package com.octopus.domain.repository

import com.octopus.config.DatabaseFactory
import com.octopus.domain.NewNotifier
import com.octopus.domain.NewUser
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDate
import java.util.Calendar
import kotlin.test.assertEquals


class NotifierRepositoryTest {

    // Used main DB as test(actually I used test db (H2) as main db)
    init {
        DatabaseFactory.connect()
        DatabaseFactory.createTables()
    }

    private val notifierRepository = NotifierRepository()
    private val userRepository = UserRepository()
    private val userStatusRepository = UserStatusRepository()

    @Test
    fun `findRegisteredUsersNotActiveToday`() = runBlocking {
        // given

        // registered user not active today
        val user1 = userRepository.create(NewUser("reza@email.com", "Reza", "Mortazavi"))
        val user1Status = userStatusRepository.create(user1.id)
        userStatusRepository.update(user1Status, LocalDate.now().minusDays(1).atStartOfDay())
        notifierRepository.create(NewNotifier(user1.id, Calendar.getInstance().get(Calendar.DAY_OF_WEEK), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))

        // registered user active today
        val user2 = userRepository.create(NewUser("rezaa@email.com", "Reza", "Mortazavi"))
        val user2Status = userStatusRepository.create(user2.id)
        userStatusRepository.update(user2Status, LocalDate.now().atStartOfDay())
        notifierRepository.create(NewNotifier(user2.id, Calendar.getInstance().get(Calendar.DAY_OF_WEEK), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))

        // not register user not active today
        val user3 = userRepository.create(NewUser("rezaaa@email.com", "Reza", "Mortazavi"))
        val user3Status = userStatusRepository.create(user3.id)
        userStatusRepository.update(user3Status, LocalDate.now().minusDays(1).atStartOfDay())

        // when
        val notActiveToday = notifierRepository.findRegisteredUsersNotActiveToday(
            Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        )

        // then
        assertEquals(1, notActiveToday.size)
        assertEquals("reza@email.com", notActiveToday[0].email)
    }
}