package com.github.ir31k0.response;

import java.util.ArrayList;

public class CheckResponse {
    public int status_code;
    public String lang;
    public boolean run_success;
    public String status_runtime;
    public int memory;
    public String display_runtime;
    public ArrayList<String> code_answer;
    public ArrayList<Object> code_output;
    public ArrayList<String> std_output_list;
    public int elapsed_time;
    public long task_finish_time;
    public String task_name;
    public int expected_status_code;
    public String expected_lang;
    public boolean expected_run_success;
    public String expected_status_runtime;
    public int expected_memory;
    public ArrayList<String> expected_code_answer;
    public ArrayList<Object> expected_code_output;
    public ArrayList<String> expected_std_output_list;
    public int expected_elapsed_time;
    public long expected_task_finish_time;
    public String expected_task_name;
    public boolean correct_answer;
    public String compare_result;
    public int total_correct;
    public int total_testcases;
    public Object runtime_percentile;
    public String status_memory;
    public Object memory_percentile;
    public String pretty_lang;
    public String submission_id;
    public String status_msg;
    public String state;
}
