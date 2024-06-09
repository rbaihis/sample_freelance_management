package com.example.user.dto.freelance;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
public class  RefundResponse {
    private boolean isRefundable= false;
    private String message="" ;

    /**
     * this.message="" in this constructor for lazy usage
     * this.isRefundable=false in this constructor for lazy usage
     */
    public RefundResponse() {
        this.isRefundable = false;
        this.message ="";
    }

    /**
     * this.message="" in this constructor for lazy usage
     * @param isRefundable boolean
     */
    public RefundResponse(boolean isRefundable) {
        this.isRefundable = isRefundable;
        this.message = "";
    }

    /**
     * this.isRefundable=false in this constructor for lazy usage
     * @param message string
     */
    public RefundResponse(String message) {
        this.isRefundable = false;
        this.message = message;
    }
    public RefundResponse(boolean isRefundable, String message) {
        this.isRefundable = isRefundable;
        this.message = message;
    }
}
