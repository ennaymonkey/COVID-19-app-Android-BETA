/*
 * Copyright © 2020 NHSX. All rights reserved.
 */

package uk.nhs.nhsx.sonar.android.app.status

import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone.UTC
import org.junit.Test
import uk.nhs.nhsx.sonar.android.app.util.nonEmptySetOf

class UserStateSerializationTest {

    val serialize = UserStateSerialization::serialize
    val deserialize = UserStateSerialization::deserialize

    @Test
    fun `serialize default state`() {
        val defaultState = DefaultState

        assertThat(serialize(defaultState))
            .isEqualTo("""{"type":"DefaultState"}""")
    }

    @Test
    fun `serialize recovery state`() {
        val defaultState = RecoveryState

        assertThat(serialize(defaultState))
            .isEqualTo("""{"type":"RecoveryState"}""")
    }

    @Test
    fun `serialize ember state`() {
        val until = DateTime(1587241302262L, UTC)
        val emberState = EmberState(until)

        assertThat(serialize(emberState))
            .isEqualTo("""{"type":"EmberState","until":1587241302262}""")
    }

    @Test
    fun `serialize red state`() {
        val until = DateTime(1587241302263L, UTC)
        val symptoms = nonEmptySetOf(Symptom.COUGH, Symptom.TEMPERATURE)
        val redState = RedState(until, symptoms)

        assertThat(serialize(redState))
            .isEqualTo("""{"type":"RedState","symptoms":["COUGH","TEMPERATURE"],"until":1587241302263}""")
    }

    @Test
    fun `serialize checkin state`() {
        val until = DateTime(1587241302263L, UTC)
        val symptoms = nonEmptySetOf(Symptom.TEMPERATURE)
        val redState = CheckinState(until, symptoms)

        assertThat(serialize(redState))
            .isEqualTo("""{"type":"CheckinState","symptoms":["TEMPERATURE"],"until":1587241302263}""")
    }

    @Test
    fun `deserialize default state`() {
        assertThat(deserialize("""{"type":"DefaultState"}"""))
            .isEqualTo(DefaultState)
    }

    @Test
    fun `deserialize default state - with legacy until timestamp`() {
        assertThat(deserialize("""{"until":1587241302261,"type":"DefaultState"}"""))
            .isEqualTo(DefaultState)
    }

    @Test
    fun `deserialize recovery state`() {
        assertThat(deserialize("""{"type":"RecoveryState"}"""))
            .isEqualTo(RecoveryState)
    }

    @Test
    fun `deserialize recovery state - with legacy until timestamp`() {
        assertThat(deserialize("""{"until":1587241302262,"type":"RecoveryState"}"""))
            .isEqualTo(RecoveryState)
    }

    @Test
    fun `deserialize ember state`() {
        val until = DateTime(1587241302262L, UTC)

        assertThat(deserialize("""{"until":1587241302262,"type":"EmberState"}"""))
            .isEqualTo(EmberState(until))
    }

    @Test
    fun `deserialize red state`() {
        val until = DateTime(1587241302262L, UTC)

        assertThat(deserialize("""{"until":1587241302262,"symptoms":["COUGH"],"type":"RedState"}"""))
            .isEqualTo(RedState(until, nonEmptySetOf(Symptom.COUGH)))
    }

    @Test
    fun `deserialize checkin state`() {
        val until = DateTime(1587241302262L, UTC)

        assertThat(deserialize("""{"until":1587241302262,"symptoms":["COUGH"],"type":"CheckinState"}"""))
            .isEqualTo(CheckinState(until, nonEmptySetOf(Symptom.COUGH)))
    }

    @Test
    fun `deserialize invalid red state`() {
        assertThat(deserialize("""{"until":1587241302262,"symptoms":[],"type":"RedState"}"""))
            .isNull()
    }
}
