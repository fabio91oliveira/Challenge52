package oliveira.fabio.challenge52.feature.goalslist.vo

import java.io.Serializable

data class ActivityResultVO(
    var hasChanged: Boolean = false,
    var type: ActivityResultTypeEnum = ActivityResultTypeEnum.NONE
) :
    Serializable {
    fun setChangeUpdated() {
        hasChanged = true
        type = ActivityResultTypeEnum.UPDATED
    }

    fun setChangeRemoved() {
        hasChanged = true
        type = ActivityResultTypeEnum.REMOVED
    }

    fun setChangeCompleted() {
        hasChanged = true
        type = ActivityResultTypeEnum.COMPLETED
    }
}

enum class ActivityResultTypeEnum {
    NONE,
    UPDATED,
    REMOVED,
    COMPLETED
}