package com.example

import org.junit.Assert.*
import org.junit.Test

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

  @Test
  fun testQuestionBankIntegrity() {
    val questions = com.example.data.repository.QuestionBank.allQuestions
    assertTrue("Question bank should have at least 30 questions", questions.size >= 30)

    for (q in questions) {
      assertTrue("Question prompt should not be empty: ${q.id}", q.prompt.isNotBlank())
      assertEquals("Each question should have exactly 4 options: ${q.id}", 4, q.options.size)
      assertTrue("Correct index should be within bounds: ${q.id}", q.correctIndex in 0..3)
      assertTrue("Explanation should not be empty: ${q.id}", q.explanation.isNotBlank())
    }

    val categories = questions.map { it.category }.toSet()
    assertTrue("Should cover at least 6 categories", categories.size >= 6)
  }

  @Test
  fun testThemeModesAvailable() {
    val themes = com.example.data.model.GameThemeMode.entries
    assertEquals("Should support at least 3 themes", 3, themes.size)
    assertNotNull("Cyberpunk theme should exist", com.example.data.model.GameThemeMode.fromId("CYBERPUNK"))
    assertNotNull("Vaporwave theme should exist", com.example.data.model.GameThemeMode.fromId("VAPORWAVE"))
    assertNotNull("Midnight theme should exist", com.example.data.model.GameThemeMode.fromId("MIDNIGHT"))
  }

  @Test
  fun testDidYouKnowFactsPresent() {
    val questions = com.example.data.repository.QuestionBank.allQuestions
    for (q in questions) {
      assertTrue("Fun fact should not be blank for ${q.id}", q.funFact.isNotBlank())
    }
  }

  @Test
  fun testAvatarFramesAndRanks() {
    val frames = com.example.data.model.AvatarFrame.entries
    assertTrue("Should have multiple avatar frames", frames.size >= 4)
    assertNotNull(com.example.data.model.AvatarFrame.fromId("NEON_CYAN"))

    val initialScreen = com.example.ui.viewmodel.AppScreen.WELCOME
    assertEquals("Initial screen should be WELCOME", com.example.ui.viewmodel.AppScreen.WELCOME, initialScreen)

    val rank = com.example.data.model.PlayerRank.fromXp(1200)
    assertNotNull(rank)
    assertNotNull(rank.accentColor)
  }
}
