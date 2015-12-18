package com.lotus.allconferencing.suites;

import com.lotus.allconferencing.services.OldScheduler_v1_Invite_Test;
import com.lotus.allconferencing.services.OldScheduler_v2_Invite_Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
    OldScheduler_v2_Invite_Test.class,
    OldScheduler_v1_Invite_Test.class
})

public class OldSchedulerMeetingSetupSuiteTest {

}
