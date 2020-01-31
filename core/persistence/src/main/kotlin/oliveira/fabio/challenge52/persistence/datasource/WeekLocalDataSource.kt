package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.Week

interface WeekLocalDataSource {
    fun addWeeks(weeks: List<Week>): List<Long>
    fun removeWeeks(weeks: List<Week>): Int
    fun updateWeek(week: Week)
    fun updateWeeks(weeks: List<Week>)
}