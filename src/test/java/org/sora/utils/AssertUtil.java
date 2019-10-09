package org.sora.utils;

import org.sora.rest.Code;
import org.sora.rest.Message;
import org.sora.rest.StatusDTO;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertUtil {

    public static void assertPositiveStatus(StatusDTO status){

        assertThat(status.getCode()).isEqualTo(Code.OK.toString());
        assertThat(status.getMessage()).isEqualTo(Message.SUCCESS);
    }
}
