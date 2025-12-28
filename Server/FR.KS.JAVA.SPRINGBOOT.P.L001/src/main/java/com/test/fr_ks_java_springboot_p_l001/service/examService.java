package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.submissions.ExamSubmitRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.submissions.ExamSubmitResponse;

public interface examService {
    ExamSubmitResponse submit(ExamSubmitRequest req);
}
