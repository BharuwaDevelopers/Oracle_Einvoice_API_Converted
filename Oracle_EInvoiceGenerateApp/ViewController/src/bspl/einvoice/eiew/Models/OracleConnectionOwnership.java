package bspl.einvoice.eiew.Models;

//package Einv_EwayBill_WebApp.Models;

public enum OracleConnectionOwnership {
    /**
     * Connection is owned and managed by SqlHelper
     */
    Internal,

    /**
     * Connection is owned and managed by the caller
     */
    External
}
