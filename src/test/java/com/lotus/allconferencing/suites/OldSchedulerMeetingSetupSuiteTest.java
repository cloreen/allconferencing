package com.lotus.allconferencing.suites;

import com.lotus.allconferencing.services.schedulers.tests.OldScheduler_v1a_Invite_Test;
import com.lotus.allconferencing.services.schedulers.tests.OldScheduler_v2a_Invite_Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        value = {
    OldScheduler_v2a_Invite_Test.class,
    OldScheduler_v1a_Invite_Test.class
})

public class OldSchedulerMeetingSetupSuiteTest {

}
