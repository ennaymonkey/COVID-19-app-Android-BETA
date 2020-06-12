package uk.nhs.nhsx.sonar.android.app.onboarding

import org.junit.Test
import uk.nhs.nhsx.sonar.android.app.testhelpers.base.EspressoTest
import uk.nhs.nhsx.sonar.android.app.testhelpers.robots.PostCodeRobot

class PostCodeActivityTest : EspressoTest() {

    private val postCodeRobot = PostCodeRobot()

    @Test
    fun pristineState() {
        testAppContext.app.startTestActivity<PostCodeActivity>()

        postCodeRobot.checkActivityIsDisplayed()
        postCodeRobot.checkTitleIsDisplayed()
        postCodeRobot.checkExampleIsDisplayed()
        postCodeRobot.checkInvalidHintIsNotDisplayed()
        postCodeRobot.checkEditTextIs("")
        postCodeRobot.checkRationaleIsVisible()
    }

    @Test
    fun emptyPostCodeShowsInvalidHint() {
        testAppContext.app.startTestActivity<PostCodeActivity>()

        postCodeRobot.clickContinue()
        postCodeRobot.checkInvalidHintIsDisplayed()
    }

    @Test
    fun invalidPostCodeShowsInvalidHint() {
        testAppContext.app.startTestActivity<PostCodeActivity>()

        postCodeRobot.enterPostCode("1")
        postCodeRobot.clickContinue()
        postCodeRobot.checkInvalidHintIsDisplayed()
    }

    @Test
    fun validPostCodeProceedsToNextView() {
        testAppContext.app.startTestActivity<PostCodeActivity>()

        postCodeRobot.enterPostCode("E1")
        postCodeRobot.clickContinue()
        postCodeRobot.checkActivityDoesNotExist()
    }
}
