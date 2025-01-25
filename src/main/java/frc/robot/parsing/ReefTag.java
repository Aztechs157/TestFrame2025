// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.parsing;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

/** Add your docs here. */
public class ReefTag {
    public int tagID;
    public String face;
    public Stage[] corals;
    public double algaeVerticalPos;

    public class Stage {
        public double verticalPos;
        public double leftOffset;
        public double rightOffset;

        public Stage(JsonNode stageJSON) {
            this.verticalPos = stageJSON.get("verticalPos").asDouble();
            this.leftOffset = stageJSON.get("horizontalOffsets").get("left").asDouble();
            this.rightOffset = stageJSON.get("horizontalOffsets").get("right").asDouble();
        }
    }

    public ReefTag(JsonNode tagJSON, int tagID) {
        this.tagID = tagID; // TODO: Try to get tag ID from tagJSON
        this.face = tagJSON.get("face").asText();
        this.algaeVerticalPos = tagJSON.get("algaeVerticalPos").asDouble();
        List<Stage> coralList = new ArrayList<Stage>(4);
        for(int i = 1; i <= 4; i++) {
            coralList.add(new Stage(tagJSON.get("coral").get("stage" + i)));
        }
        coralList.toArray(corals);
    }

    /**
     * Get vertical position for the algae
     * @return a double representing the vertical position
     */
    public double getAlgaeVerticalPos() {
        return algaeVerticalPos;
    }
    /**
     * Get vertical position for the specified stage
     * @param stage - one-indexed ie: 1,2,3,4
     * @return a double representing the vertical position
     */
    public double getCoralVerticalPos(int stage) {
        return corals[stage - 1].verticalPos;
    }
    /**
     * Get left offset for the specified stage
     * @param stage - one-indexed ie: 1,2,3,4
     * @return a double representing the left offset
     */
    public double getLeftOffset(int stage) {
        return corals[stage - 1].leftOffset;
    }
    /**
     * Get right offset for the specified stage
     * @param stage - one-indexed ie: 1,2,3,4
     * @return a double representing the right offset
     */
    public double getRightOffset(int stage) {
        return corals[stage - 1].rightOffset;
    }
}