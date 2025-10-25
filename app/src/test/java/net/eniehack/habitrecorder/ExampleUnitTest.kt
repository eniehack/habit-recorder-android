package net.eniehack.habitrecorder

import net.eniehack.habitrecorder.data.CheckIn
import net.eniehack.habitrecorder.ui.calcStreaks
import org.junit.Test

import org.junit.Assert.*
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
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkIns = listOf(
            CheckIn(habitId = 1, createdAt = "2000-01-01"),
            CheckIn(habitId = 1, createdAt = "2000-01-02"),
            CheckIn(habitId = 1, createdAt = "2000-01-03")
        )
        assertEquals(3, calcStreaks(checkIns, LocalDate.of(2000, 1, 3), dateFormat))
    }

    @Test
    fun testCalcStreak2() {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkIns = listOf<CheckIn>()
        assertEquals(0, calcStreaks(checkIns, LocalDate.of(2000, 1, 3), dateFormat))
    }
    @Test
    fun testCalcStreak3() {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkIns = listOf(
            CheckIn(habitId = 1, createdAt = "2000-01-01"),
            CheckIn(habitId = 1, createdAt = "2000-01-03")
        )
        assertEquals(0, calcStreaks(checkIns, LocalDate.of(2000, 1, 4), dateFormat))
    }
    @Test
    fun testCalcStreak4() {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkIns = listOf(
            CheckIn(habitId = 1, createdAt = "2000-01-01"),
            CheckIn(habitId = 1, createdAt = "2000-01-03")
        )
        assertEquals(1, calcStreaks(checkIns, LocalDate.of(2000, 1, 3), dateFormat))
    }
}
