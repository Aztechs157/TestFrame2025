// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.parsing;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;

import edu.wpi.first.wpilibj.Filesystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Add your docs here. */
public class PositionDetails {
    private final String JSONPath = "/position_details.json";
    private final double ARBITRARY_TAG_ID = 6;
    public Dictionary<Integer, ReefTag> reefTags = new Hashtable<>();

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
     * Get vertical position for the specified stage for the specified tag
     * @param stage - one-indexed ie: 1,2,3,4
     * @return a double representing the vertical position
     */
    public double getCoralVerticalPos(int stage) {
        return reefTags.get(ARBITRARY_TAG_ID).getCoralVerticalPos(stage);
    }
    /**
     * Get left offset for the specified stage for the specified tag
     * @param stage - one-indexed ie: 1,2,3,4
     * @return a double representing the left offset
     */
    public double getLeftOffset(int stage) {
        return reefTags.get(ARBITRARY_TAG_ID).getLeftOffset(stage);
    }
    /**
     * Get right offset for the specified stage for the specified tag
     * @param stage - one-indexed ie: 1,2,3,4
     * @return a double representing the right offset
     */
    public double getRightOffset(int stage) {
        return reefTags.get(ARBITRARY_TAG_ID).getRightOffset(stage);
    }

}
