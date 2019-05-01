package oliveira.fabio.challenge52.persistence.source

import android.content.Context
import androidx.room.Room

fun provideBuilder(context: Context) = Room.databaseBuilder(
    context,
    Database::class.java,
    "52Challenge.db"
)
    .build()