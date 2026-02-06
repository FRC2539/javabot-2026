package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.Interpolatable;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;
import edu.wpi.first.wpilibj2.command.Command;

/* The only real complexity in this command comes from finding out what offset we need to apply to the hub position to "account" for our robot's velocity
 * If we're moving towards the right side of the field, then our robot's rightwards speed is (from a strictly field-relative perspective)
 * causing our ball to drift. If we were to shoot normally, the rightwards velocity imparted onto the ball from the robot
 * would cause the ball to miss and land to the right of the hub. This issue applies regardless of what direction we're moving in,
 * including if we're moving towards/away from the hub. Besides this iteration we need to do to find the virtual position, the logic is
 * mostly all the same.
 */

/*
 * - filter our robot's X and Y velocity to hopefully prevent some turret jitter.
 * - Convert our robot's position and velocity to our turret's velocity and position
 * - Calculate our turret's position and velocity at the time we shoot. (mostly just latency)
 * - get the translation2d between our turret and the hub.
 * - Use loop to converge on the needed virtual position (see furthur comments below).
 * - get the translation2d between the turret and this new virtual position.
 * - The angle of this vector can be used to find the angle the turret needs to rotate to.
 * - the straightline distance (.getNorm()) between the two points can be plugged into our table to get the needed hood angle.
 */
public class ShootOnTheFlyCommand extends Command {

  public record ShotSettings(Double timeOfFlight, Double hoodAngle)
      implements Interpolatable<ShotSettings> {
    @Override
    public ShotSettings interpolate(ShotSettings endValue, double t) {
      return new ShotSettings(
          MathUtil.interpolate(this.timeOfFlight, endValue.timeOfFlight, t),
          MathUtil.interpolate(this.hoodAngle, endValue.hoodAngle, t));
    }
  }

  // 0.1 second buffer, 0.1 / 0.02 (50hz) = 5, taken from 6328.
  private final LinearFilter vxFilter = LinearFilter.movingAverage(5);
  private final LinearFilter vyFilter = LinearFilter.movingAverage(5);

  private final InterpolatingTreeMap<Double, ShotSettings> shotMap =
      new InterpolatingTreeMap<>(InverseInterpolator.forDouble(), ShotSettings::interpolate);

  private double latency = 0.020;
  private Translation2d robotPosition;
  private Translation2d hubPosition;
  private Translation2d robotVelocity;
  private Rotation2d angularRobotVelocity;
  private Rotation2d robotRotation;
  private final Translation2d turretOffset = new Translation2d(-0.127, 0.0);

  public ShootOnTheFlyCommand() {}

  @Override
  public void initialize() {}

  @Override
  public void execute() {

    /* pass our robot velocity through a linear filter to smooth out encoder noise and hopefully reduce turret/hoot jitter, inspired by 6328 */
    double smoothedVx = vxFilter.calculate(robotVelocity.getX());
    double smoothedVy = vyFilter.calculate(robotVelocity.getY());
    Translation2d filteredRobotVelocity = new Translation2d(smoothedVx, smoothedVy);

    /* We currently have a field relative position and field relative velocity to the *robot*,
        but for all our calculations later we're only really concerned with the turret's velocity and position relative to the hub,
        so we convert from one to the other using our turrets offset to the center.
    */

    // 1. Convert robot relative turret offset to a field relative translation from the robot.
    Translation2d turretOffsetFieldRelative = turretOffset.rotateBy(robotRotation);
    // 2. Get the direction of the "whip" by rotating the offset 90 degrees
    Translation2d tangentialDirection =
        turretOffsetFieldRelative.rotateBy(Rotation2d.fromDegrees(90));
    // 3. Scale that  by the angular velocity (radians per second)

    Translation2d tangentialVelocity = tangentialDirection.times(angularRobotVelocity.getRadians());

    Translation2d turretFieldVelocity = filteredRobotVelocity.plus(tangentialVelocity);

    Translation2d futureTurretPos =
        robotPosition.plus(turretOffsetFieldRelative).plus(turretFieldVelocity.times(latency));

    Translation2d realDisplacementToHub = hubPosition.minus(futureTurretPos);

    double realDistance = realDisplacementToHub.getNorm();
    double estimatedFlightTime = shotMap.get(realDistance).timeOfFlight;

    /* When we're moving, the ball inherits the robot's speed when we shoot (relative to the field).
     * If we plug our *actual* distance from the hub (realDistance) into our shot map,
     * we'll miss because our robot's velocity has changed the ball's trajectory.
     * The fundamental problem here is that our [Distance -> Hood Angle] map assumes that our robot is *stationary* when we shoot.
     */

    /* To fix this, we calculate a virtual target & distance, a coordinate that is offset from the "real" hub based on our robot's velocity
     * By aiming at this target and setting the hood angle based on our distance to it, we cancel out the velocity inherited
     * from the robot's movement, ensuring the ball's field-relative trajectory ends at the actual hub.
     */

    /* This problem is a circular dependency however, to find the Virtual Distance
     * we need the ball's "real" TOF (using our current "real" robot pos & velocity),
     * But since we're moving, we can't use our [Distance -> ToF] map.
     * The loop below converges on a virtual distance that when plugged into our shotMap,
     * gives us an adjusted hood angle that cancels out our robot's field velocity and leaves us with the "correct" shot to the hub.
     */

    /* adapted from 1690's software presentation from 2024, the equation for this virtual distance is transcendental and has to be approximated.
     * https://www.youtube.com/watch?v=vUtVXz7ebEE&
     */

    Translation2d virtualTarget = hubPosition;
    double virtualDistance = realDistance;
    for (int i = 0; i < 4; i++) { // 4 iterations
      virtualTarget = hubPosition.minus(turretFieldVelocity.times(estimatedFlightTime));

      virtualDistance = futureTurretPos.getDistance(virtualTarget);

      double newFlightTime = shotMap.get(virtualDistance).timeOfFlight;

      if (Math.abs(newFlightTime - estimatedFlightTime) < 0.02) break;
      estimatedFlightTime = newFlightTime;
    }

    Translation2d aimingVector = virtualTarget.minus(futureTurretPos);

    Rotation2d fieldRelativeTurretAngle = aimingVector.getAngle();

    Rotation2d robotRelativeTurretAngle = fieldRelativeTurretAngle.minus(robotRotation);

    double neededHoodAngle = shotMap.get(virtualDistance).hoodAngle;
  }
}
