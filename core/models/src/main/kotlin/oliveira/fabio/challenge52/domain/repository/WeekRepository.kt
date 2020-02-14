package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.persistence.model.entity.Week

interface WeekRepository {
    fun removeWeeks(weeks: List<Week>): Int
    fun updateWeek(week: Week)
    fun updateWeeks(weeks: List<Week>)
    fun addWeeks(weeks: List<Week>): List<Long>
}