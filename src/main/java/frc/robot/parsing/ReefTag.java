// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.parsing;

import com.fasterxml.jackson.databind.JsonNode;

/** Add your docs here. */
public class ReefTag {
    public int tagID;
    public String face;
    public double algaeVerticalPos;

    public ReefTag(JsonNode tagJSON, int tagID) {
        this.tagID = tagID; // TODO: Try to get tag ID from tagJSON
        this.face = tagJSON.get("face").asText();
        this.algaeVerticalPos = tagJSON.get("algaeVerticalPos").asDouble();
    }

    /**
     * Get vertical position for the algae
     * @return a double representing the vertical position
     */
    public double getAlgaeVerticalPos() {
        return algaeVerticalPos;
    }
}