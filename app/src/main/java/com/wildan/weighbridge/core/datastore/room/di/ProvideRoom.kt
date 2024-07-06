package com.wildan.weighbridge.core.datastore.room.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wildan.weighbridge.core.datastore.room.converter.Converters
import com.wildan.weighbridge.core.datastore.room.dao.TicketDao
import com.wildan.weighbridge.core.model.PendingActionItem
import com.wildan.weighbridge.core.model.TicketItem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.io.File

/*
 * Created by Wildan Nafian on 31/01/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@Module
@InstallIn(SingletonComponent::class)
object ProvideRoom {

    private const val LIB_NAME = "sqlcipher"
    private const val DB = "yourname.db"
    private const val FLAVOUR = "as76bd0d8213nf"

    @Database(entities = [TicketItem::class, PendingActionItem::class], version = 1, exportSchema = false)
    @TypeConverters(Converters::class)
    abstract class AppDatabase : RoomDatabase() {

        abstract fun ticketDao(): TicketDao
    }

    @Provides
    fun provideEncryptedRoom(@ApplicationContext context: Context): TicketDao {
        System.loadLibrary(LIB_NAME)
        val databaseFile: File = context.getDatabasePath(DB)
        val factory = SupportOpenHelperFactory(FLAVOUR.encodeToByteArray())

        val room = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseFile.absolutePath
        ).openHelperFactory(factory).build()

        return room.ticketDao()
    }

}
