package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.persistence.model.entity.Week

interface WeekRepository {
    suspend fun removeWeeks(weeks: List<Week>): Int
    suspend fun updateWeek(week: Week)
    suspend fun updateWeeks(weeks: List<Week>)
    suspend fun addWeeks(weeks: List<Week>): List<Long>
}