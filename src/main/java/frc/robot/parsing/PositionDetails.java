// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.parsing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import edu.wpi.first.wpilibj.Filesystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Add your docs here. */
public class PositionDetails {
    private final String JSONPath = "/position_details.json";
    public Stage[] corals;
    public Dictionary<Integer, ReefTag> reefTags = new Hashtable<>();

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

    public PositionDetails() {
        File file = new File(Filesystem.getDeployDirectory().toPath() + JSONPath);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode json = objectMapper.readTree(file);
            for (Iterator<String> i = json.get("reef").fieldNames(); i.hasNext();) {
                String currentTag = i.next();
                int tagID = Integer.parseInt(currentTag.substring(3));
                reefTags.put(tagID, new ReefTag(json.get("reef").get(currentTag), tagID));
            }
            List<Stage> coralList = new ArrayList<Stage>(4);
            for(int i = 1; i <= 4; i++) {
                coralList.add(new Stage(json.get("reef").get("coral").get("stage" + i)));
            }
            coralList.toArray(corals);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Get vertical position for the algae for the specified tag
     * @param tagID - red oriented tag IDs ie: blue 17 -> red 6
     * @return a double representing the vertical position
     */
    public double getAlgaeVerticalPos(int tagID) {
        return reefTags.get(tagID).getAlgaeVerticalPos();
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
