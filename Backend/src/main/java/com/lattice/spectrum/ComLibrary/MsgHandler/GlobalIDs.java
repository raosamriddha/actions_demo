/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler;

/**
 * class containing packet id and length definitions
 *
 * @author Anuj Pathak
 */
public class GlobalIDs {
    public static final byte INVALID_ID = 0;
    public static final byte INVALID_LENGTH = 0;
    /**
     * Pkt IDS to be received from KrosFlo
     */
    public static final byte SCALE_INFORMATION_ID = 2;
    public static final byte SCALE_INFORMATION_LENGTH = 16;
    public static final byte PRESSURE_READINGS_ID = 11;
    public static final byte PRESSURE_READINGS_LENGTH = 10;
    public static final byte MAIN_PUMP_STATUS_ID = 26;
    public static final byte MAIN_PUMP_STATUS_LENGTH = 14;
    public static final byte SCALE_TYPE_LIST_ID = 5;
    public static final byte SCALE_TYPE_LIST_LENGTH = 18;
    public static final byte AUX_PUMP_TYPE_LIST_ID = 7;
    public static final byte AUX_PUMP_TYPE_LIST_LENGTH = 18;
    public static final byte PRESSURE_TARE_DONE_ID = 31;
    public static final byte PRESSURE_TARE_DONE_LENGTH = 1;
    public static final byte CALIBRATE_PRESSURE_DONE_ID = 38;
    public static final byte CALIBRATE_PRESSURE_DONE_LENGTH = 5;
    public static final byte SCALE_TARE_DONE_ID = 36;
    public static final byte SCALE_TARE_DONE_LENGTH = 2;
    public static final byte AUX_PUMP_STATUS_ID = 10;
    public static final byte AUX_PUMP_STATUS_LENGTH = 12;
    public static final byte CONDUCTIVITY_INFO_ID = 14;
    public static final byte CONDUCTIVITY_INFO_LENGTH = 14;
    public static final byte VALVE_POSITION_ID = 16;
    public static final byte VALVE_POSITION_LENGTH = 13;
    public static final byte PRESSURE_CHANNEL_GAIN_ID = 28;
    public static final byte PRESSURE_CHANNEL_GAIN_LENGTH = 25;
    public static final byte LIMITING_VALUE_ID = 34;
    public static final byte LIMITING_VALUE_LENGTH = 15;
    public static final byte REPORT_ERROR_ID = 18;
    public static final byte REPORT_ERROR_LENGTH = 3;
    public static final byte REPORT_ERROR_TEXT_ID = 20;
    public static final byte REPORT_ERROR_TEXT_LENGTH = 19;
    public static final byte REPORT_TUBING_CALIBRATION_RESULT_ID = 23;
    public static final byte REPORT_TUBING_CALIBRATION_RESULT_LENGTH = 7;
    public static final byte START_HEART_BEAT_ID=45;
    public static final byte PUMP_SPEED_TEXT_LENGTH =21;
    public static final byte PUMP_SPEED_TEXT_ID =44;
    public static final byte PUMP_ALARM_INDICATOR_LENGTH =22;
    public static final byte PUMP_ALARM_INDICATOR_ID =43;
    public static final byte PUMP_SERIAL_NUMBER=50;

    /**
     * Pkt ids to be sent by PC
     */
    public static final byte SET_MAIN_PUMP_SPEED_ID = 1;
    public static final byte SET_MAIN_PUMP_SPEED_LENGTH = 5;
    public static final byte START_PRESSURE_TARE_ID = 30;
    public static final byte START_PRESSURE_TARE_LENGTH = 1;
    public static final byte CALIBRATE_PRESSURE_ID = 37;
    public static final byte CALIBRATE_PRESSURE_LENGTH = 4;
    public static final byte START_SCALE_TARE_ID = 36;
    public static final byte PUMP_KEY_ACTION_ID = 47;
    public static final byte PUMP_KEY_ACTION_LENGTH = 5;
    public static final byte START_SCALE_TARE_LENGTH = 2;
    public static final byte REQUEST_SCALE_LIST_ID = 4;
    public static final byte REQUEST_SCALE_LIST_LENGTH = 2;
    public static final byte REQUEST_AUX_PUMP_LIST_ID = 6;
    public static final byte REQUEST_AUX_PUMP_LIST_LENGTH = 2;
    public static final byte SET_AUX_PUMP_SPEED_ID = 9;
    public static final byte SET_AUX_PUMP_SPEED_LENGTH = 8;
    public static final byte SET_SCALE_TYPE_ID = 13;
    public static final byte SET_SCALE_TYPE_LENGTH = 3;
    public static final byte SET_VALVE_POSITION_ID = 15;
    public static final byte SET_VALVE_POSITION_LENGTH = 12;
    public static final byte REQUEST_PRESSURE_CHANNEL_GAIN_ID = 27;
    public static final byte REQUEST_PRESSURE_CHANNEL_GAIN_LENGTH = 1;
    public static final byte SET_PRESSURE_CHANNEL_GAIN_ID = 29;
    public static final byte SET_PRESSURE_CHANNEL_GAIN_LENGTH = 25;
    public static final byte REQUEST_LIMITING_VALUE_ID = 33;
    public static final byte REQUEST_LIMITING_VALUE_LENGTH = 1;
    public static final byte REQUEST_ERROR_TEXT_ID = 19;
    public static final byte REQUEST_ERROR_TEXT_LENGTH = 3;
    public static final byte REQUEST_TUBING_CALIBRATION_ID = 21;
    public static final byte REQUEST_TUBING_CALIBRATION_LENGTH = 8;
    public static final byte REQUEST_TUBING_CALIBRATION_RESULT_ID = 22;
    public static final byte REQUEST_TUBING_CALIBRATION_RESULT_LENGTH = 1;
    public static final byte SET_PRESSURE_UNIT_ID = 25;
    public static final byte SET_PRESSURE_UNIT_LENGTH = 2;
    public static final byte CONFIGURE_AUXILIARY_ID = 17;
    public static final byte CONFIGURE_AUXILIARY_LENGTH = 3;

    /**
     *
     */
    public static final byte RESET_PROCESSOR_ID = (byte) 177;
    public static final byte RESET_PROCESSOR_LENGTH = 5;
    public static final byte MAIN_CPU_ENABLE_PROGRAMMING_ID = (byte) 178;
    public static final byte MAIN_CPU_ENABLE_PROGRAMMING_LENGTH = 12;
    public static final byte MAIN_CPU_ERASE_SECTOR_ID = (byte) 179;
    public static final byte MAIN_CPU_ERASE_SECTOR_LENGTH = 6;
    public static final byte MAIN_CPU_FLASH_DATA_ID = (byte) 180;
    public static final byte MAIN_CPU_FLASH_DATA_LENGTH = 39;
    public static final byte MAIN_CPU_WRITE_FLASH_ID = (byte) 181;
    public static final byte MAIN_CPU_WRITE_FLASH_LENGTH = 10;
    public static final byte MAIN_CPU_FLASH_COMMAND_COMPLETE_ID = (byte) 182;
    public static final byte MAIN_CPU_FLASH_COMMAND_COMPLETE_LENGTH = 9;
    public static final byte MAIN_CPU_RESET_COMMAND_ID = (byte) 184;
    public static final byte MAIN_CPU_RESET_COMMAND_LENGTH = 14;
    /**
     *
     */
    public static final byte RESET_IPROCESSOR_ID = (byte) 203;
    public static final byte RESET_IPROCESSOR_LENGTH = 5;
    public static final byte INTERFACE_CPU_FLASH_COMMAND_COMPLETE_ID = (byte) 204;
    public static final byte INTERFACE_CPU_FLASH_COMMAND_COMPLETE_LENGTH = 7;
    public static final byte INTERFACE_CPU_RESET_COMMAND_ID = (byte) 205;
    public static final byte INTERFACE_CPU_RESET_COMMAND_LENGTH = 15;
    public static final byte INTERFACE_CPU_ENABLE_PROGRAMMING_ID = (byte) 200;
    public static final byte INTERFACE_CPU_ENABLE_PROGRAMMING_LENGTH = 17;

    public static byte START_HEART_BEAT_LENGTH=1;
    public static byte REQUEST_PUMP_SERIAL_NUMBER_LENGTH=1;
    public static byte RESULT_PUMP_SERIAL_NUMBER_LENGTH=17;
}
