/*----------------------------------------------------------------------------*/
/* Copyright (c) 2016-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;


/**
 * The base interface for objects that can be sent over the network through network tables.
 */
public interface Sendable {


  /**
   * Initializes this {@link Sendable} object.
   *
   * @param builder sendable builder
   */
  void initSendable(SendableBuilder builder);
}
