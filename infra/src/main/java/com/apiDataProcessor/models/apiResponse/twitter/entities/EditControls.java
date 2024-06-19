package com.apiDataProcessor.models.apiResponse.twitter.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class EditControls {
    public static final String EDITS_REMAINING = "edits_remaining";
    public static final String EDIT_ELIGIBLE = "is_edit_eligible";
    public static final String EDITABLE_UNTIL = "editable_until";

    @JsonProperty(EDITS_REMAINING)
    private Integer editsRemaining;

    @JsonProperty(EDIT_ELIGIBLE)
    private Boolean editEligible;

    @JsonProperty(EDITABLE_UNTIL)
    private Timestamp editableUntil;
}
