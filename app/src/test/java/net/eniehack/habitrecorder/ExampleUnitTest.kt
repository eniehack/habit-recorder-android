package net.eniehack.habitrecorder

import net.eniehack.habitrecorder.data.CheckIn
import net.eniehack.habitrecorder.ui.calcStreaks
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class ExampleStreakTest {
    @Test
    fun testCalcStreak1() {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        val now = Instant.now()
        val checkIns = listOf(
            CheckIn(
                habitId = 1,
                date = "20000101",
                createdAt = now,
                updatedAt = now
            ),
            CheckIn(
                habitId = 1,
                date = "20000102",
                createdAt = now,
                updatedAt = now
            ),
            CheckIn(
                habitId = 1,
                date = "20000103",
                createdAt = now,
                updatedAt = now
            )
        )
        assertEquals(3, calcStreaks(checkIns, LocalDate.of(2000, 1, 3), dateFormat))
    }

    @Test
    fun testCalcStreak2() {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        val checkIns = listOf<CheckIn>()
        assertEquals(0, calcStreaks(checkIns, LocalDate.of(2000, 1, 3), dateFormat))
    }
    @Test
    fun testCalcStreak3() {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        val now = Instant.now()
        val checkIns = listOf(
            CheckIn(
                habitId = 1,
                date = "20000101",
                createdAt = now,
                updatedAt = now
            ),
            CheckIn(
                habitId = 1,
                date = "20000103",
                createdAt = now,
                updatedAt = now
            )
        )
        assertEquals(0, calcStreaks(checkIns, LocalDate.of(2000, 1, 4), dateFormat))
    }
    @Test
    fun testCalcStreak4() {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        val now = Instant.now()
        val checkIns = listOf(
            CheckIn(
                habitId = 1,
                date = "20000101",
                createdAt = now,
                updatedAt = now
            ),
            CheckIn(
                habitId = 1,
                date = "20000103",
                createdAt = now,
                updatedAt = now
            )
        )
        assertEquals(1, calcStreaks(checkIns, LocalDate.of(2000, 1, 3), dateFormat))
    }
}
