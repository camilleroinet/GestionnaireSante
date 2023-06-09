package com.example.gestionnairesante.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestionnairesante.database.dao.glycemie.GlycemieDao
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteDao
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteData
import com.example.gestionnairesante.database.dao.innerRepas.InnerPeriodeRepasDao
import com.example.gestionnairesante.database.dao.innerRepas.InnerPeriodeRepasData
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuDao
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuData
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsDao
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsData
import com.example.gestionnairesante.database.dao.innerStats.StatsDao
import com.example.gestionnairesante.database.dao.innerStats.StatsGlyPer
import com.example.gestionnairesante.database.dao.insuline.InsulineDao
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.insuline.ParamStyloDao
import com.example.gestionnairesante.database.dao.insuline.ParamStyloData
import com.example.gestionnairesante.database.dao.repas.RepasDao
import com.example.gestionnairesante.database.dao.repas.RepasData
import com.example.gestionnairesante.database.dao.periode.PeriodeDao
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.plats.PlatDao
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.database.dao.poids.PoidsDao
import com.example.gestionnairesante.database.dao.poids.PoidsData

@Database(
    entities = [
        StatsGlyPer::class,
        InnerPeriodeRepasData::class,
        ParamStyloData::class,
        InnerPlatMenuData::class,
        InnerPoidsData::class,
        InnerDiabeteData::class,
        PeriodeData::class,
        RepasData::class,
        PlatData::class,
        GlycemieData::class,
        InsulineData::class,
        PoidsData::class],
    version = 1,
    exportSchema = false
)
//@Database(entities = [PeriodeData::class,GlycemieData::class], version =2, exportSchema = true,   autoMigrations = [AutoMigration (from = 1, to = 2)])

abstract class DB_sante : RoomDatabase() {
    abstract val tabGlycemie: GlycemieDao
    abstract val tabInsuline: InsulineDao
    abstract val tabPoids: PoidsDao
    abstract val tabPlat: PlatDao
    abstract val tabMenu: RepasDao
    abstract val tabPeriode: PeriodeDao
    abstract val tabStylo: ParamStyloDao

    abstract val tabRelationnelDiabete: InnerDiabeteDao
    abstract val tabRelationnelPoids: InnerPoidsDao
    abstract val tabRelationnelPlat: InnerPlatMenuDao
    abstract val tabRelationnelMenu: InnerPeriodeRepasDao

    abstract val tabRelationnelStats: StatsDao


    companion object {
        @Volatile
        private var INSTANCE: DB_sante? = null
        fun getInstance(context: Context): DB_sante {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DB_sante::class.java,
                        "db_sante_01"
                    ).allowMainThreadQueries().build()
                }
                return instance
            }
        }
    }
}