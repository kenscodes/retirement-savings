package com.retirement.dto;

import com.retirement.model.Expense;

import java.util.List;
import java.util.Map;

/** Response DTO for /transactions/validator and /transactions/filter */
public class ValidatorResponse {
    private List<Object> valid;
    private List<Object> invalid;

    public ValidatorResponse() {}
    public ValidatorResponse(List<Object> valid, List<Object> invalid) {
        this.valid = valid;
        this.invalid = invalid;
    }

    public List<Object> getValid() { return valid; }
    public void setValid(List<Object> valid) { this.valid = valid; }
    public List<Object> getInvalid() { return invalid; }
    public void setInvalid(List<Object> invalid) { this.invalid = invalid; }
}
