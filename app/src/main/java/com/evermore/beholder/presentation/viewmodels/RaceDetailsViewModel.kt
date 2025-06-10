package com.evermore.beholder.presentation.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evermore.beholder.R
import com.evermore.beholder.data.models.OptionDisplayData
import com.evermore.beholder.data.models.Race
import com.evermore.beholder.data.models.RaceDetailItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class RaceDetailsViewModel(private val stringProvider: (Int, Any?) -> String) : ViewModel() {

    private val _raceData = MutableLiveData<Race>()
    val raceData: LiveData<Race> = _raceData

    private val _raceDetailItems = MutableLiveData<List<RaceDetailItem>>()
    val raceDetailItems: LiveData<List<RaceDetailItem>> = _raceDetailItems

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val raceAdapter: JsonAdapter<Race> = moshi.adapter(Race::class.java)

    fun loadRaceData(jsonString: String) {
        try {
            val race = raceAdapter.fromJson(jsonString)
            race?.let {
                _raceDetailItems.postValue(mapRaceToDetailItems(it))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun mapRaceToDetailItems(data: Race): List<RaceDetailItem> {
        val items = mutableListOf<RaceDetailItem>()

        items.add(RaceDetailItem.Header(data.name))

        items.add(
            RaceDetailItem.CollapsibleText(
                R.string.race_speed_text,
                data.speed.toString() + " ft"
            )
        ) // Передаем данные, не форматированную строку

        val abilityBonusesContent = data.abilityBonuses.joinToString("\n") {
            "${it.abilityScore.name}: +${it.bonus}"
        }
        items.add(
            RaceDetailItem.CollapsibleText(
                R.string.race_ability_bonuses_text,
                abilityBonusesContent
            )
        )

        data.abilityBonusOptions?.let { options ->
            val optionDisplayList = options.from.options.map { option ->
                val abilityName = option.abilityScore?.name ?: "Unknown"
                val bonus = option.bonus ?: 0
                OptionDisplayData(
                    name = "$abilityName: +$bonus",
                    description = null
                )
            }
            items.add(
                RaceDetailItem.OptionsList(
                    R.string.race_ability_bonus_options_text,
                    R.string.race_ability_bonus_options_text.toString(),
                    optionDisplayList,
                    options.choose
                )
            )
        }

        items.add(RaceDetailItem.CollapsibleText(R.string.race_alignment_text, data.alignment))

        items.add(RaceDetailItem.CollapsibleText(R.string.race_age_text, data.age))

        items.add(RaceDetailItem.CollapsibleText(R.string.race_size_text, data.sizeDescription))


        val startingProficienciesContent = if (data.startingProficiencies.isNotEmpty()) {
            data.startingProficiencies.joinToString("\n") { "• ${it.name}" }
        } else {
            stringProvider(R.string.race_no_starting_proficiencies, null)
        }
        items.add(
            RaceDetailItem.CollapsibleText(
                R.string.race_starting_proficiencies_text,
                startingProficienciesContent
            )
        )


        data.startingProficiencyOptions?.let { options ->
            val optionDisplayList = options.from.options.map { option ->
                OptionDisplayData(
                    name = option.item?.name ?: "Unknown Skill",
                    description = null
                )
            }
            items.add(
                RaceDetailItem.OptionsList(
                    R.string.race_starting_proficiency_options_text,
                    R.string.race_starting_proficiency_options_text.toString(), // Заголовок
                    optionDisplayList,
                    options.choose
                )
            )
        }

        val languagesContent = "${data.languageDesc}\n\n" +
                data.languages.joinToString("\n") { "• ${it.name}" }
        items.add(RaceDetailItem.CollapsibleText(R.string.race_languages_text, languagesContent))

        data.languageOptions?.let { options ->
            val optionDisplayList = options.from.options.map { option ->
                OptionDisplayData(
                    name = option.item?.name ?: "Unknown Language",
                    description = null
                )
            }
            items.add(
                RaceDetailItem.OptionsList(
                    R.string.race_language_options_text,
                    R.string.race_language_options_text.toString(), // Заголовок
                    optionDisplayList,
                    options.choose
                )
            )
        }

        val traits = data.traits.joinToString("\n") { "• ${it.name}" }

        items.add(
            RaceDetailItem.CollapsibleText(
                stringResId = R.string.race_traits_text,
                content = traits.toString()
            )
        )

        return items
    }
}