// com/evermore/beholder/presentation/viewmodels/MonsterDetailsViewModel.kt
package com.evermore.beholder.presentation.viewmodels

// import com.evermore.beholder.data.models.BestiaryResponse // Этот импорт не используется
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evermore.beholder.R
import com.evermore.beholder.data.models.Monster
import com.evermore.beholder.data.repositories.MonsterRepository
import com.evermore.beholder.presentation.models.MonsterDetailItem
import kotlinx.coroutines.launch

// import com.squareup.moshi.JsonAdapter // Эти импорты Moshi не используются для десериализации здесь
// import com.squareup.moshi.Moshi
// import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class MonsterDetailsViewModel(
    private val monsterRepository: MonsterRepository,
    private val stringProvider: (Int, Any?) -> String
) : ViewModel() {

    private val _monsterData = MutableLiveData<Monster>()
    val monsterData: LiveData<Monster> = _monsterData

    private val _monsterDetailItems = MutableLiveData<List<MonsterDetailItem>>()
    val monsterDetailItems: LiveData<List<MonsterDetailItem>> = _monsterDetailItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadMonsterDetails(monsterIndex: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val monster = monsterRepository.getMonsterDetails(monsterIndex)
                _monsterData.postValue(monster)
                monster.let {
                    _monsterDetailItems.postValue(mapMonsterToDetailItems(it))
                }
            } catch (e: Exception) {
                _error.value = "Error loading details: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun mapMonsterToDetailItems(data: Monster): List<MonsterDetailItem> {
        val items = mutableListOf<MonsterDetailItem>()

        // Header (имя монстра) - name всегда есть
        items.add(MonsterDetailItem.Header(data.name))

        // Basic Info
        items.add(MonsterDetailItem.CollapsibleText(
            stringResId = R.string.monster_basic_info_title,
            content = String.format(stringProvider(R.string.monster_basic_info_template, null),
                data.size,
                data.type,
                data.subtype ?: stringProvider(R.string.not_available, null), // Добавлена обработка null
                data.alignment),
            isExpanded = true // По умолчанию развернуто
        ))

        // Armor Class
        // Если armorClass может быть null, .joinToString() вызовется только если не null.
        // Иначе будет использоваться значение по умолчанию.
        val armorClassContent = data.armorClass?.joinToString("\n") { ac ->
            when (ac.type) {
                "dex" -> String.format(stringProvider(R.string.armor_class_dex_template, null),
                    ac.value)
                "spell" -> String.format(stringProvider(R.string.armor_class_spell_template, null),
                    ac.value, ac.spell?.name ?: stringProvider(R.string.not_available, null)) // ac.spell тоже nullable
                else -> String.format(stringProvider(R.string.armor_class_default_template, null),
                    ac.value, ac.type
                )
            }
        } ?: stringProvider(R.string.not_available, null) // Значение по умолчанию, если armorClass null
        items.add(MonsterDetailItem.CollapsibleText(R.string.monster_armor_class_title, armorClassContent))

        // Hit Points - Эти поля не были изменены на nullable в Monster.kt, поэтому они безопасны
        items.add(MonsterDetailItem.CollapsibleText(
            stringResId = R.string.monster_hit_points_title,
            content = String.format(stringProvider(R.string.monster_hit_points_template, null),
                data.hitPoints, data.hitDice, data.hitPointsRoll)
        ))

        // Speed
        // data.speed теперь nullable, а walk внутри speed тоже nullable
        items.add(MonsterDetailItem.CollapsibleText(
            stringResId = R.string.monster_speed_title,
            content = data.speed?.walk ?: stringProvider(R.string.not_available, null) // Обработка null для speed и walk
        ))

        // Stat Block - Эти поля не были изменены на nullable в Monster.kt
        items.add(MonsterDetailItem.StatBlock(
            str = data.strength,
            dex = data.dexterity,
            con = data.constitution,
            int = data.intelligence,
            wis = data.wisdom,
            cha = data.charisma
        ))

        // Proficiencies
        // data.proficiencies теперь nullable
        if (data.proficiencies?.isNotEmpty() == true) { // Безопасная проверка на null и пустоту
            val proficienciesContent = data.proficiencies.joinToString("\n") { prof ->
                String.format(stringProvider(R.string.monster_proficiency_template, null),
                    prof.proficiency.name, prof.value)
            }
            items.add(MonsterDetailItem.CollapsibleText(R.string.monster_proficiencies_title,
                proficienciesContent))
        } else {
            items.add(MonsterDetailItem.CollapsibleText(R.string.monster_proficiencies_title,
                stringProvider(R.string.monster_no_proficiencies, null)))
        }

        // Senses
        // data.senses теперь nullable, и passivePerception внутри senses тоже nullable
        items.add(MonsterDetailItem.CollapsibleText(
            stringResId = R.string.monster_senses_title,
            content = String.format(stringProvider(R.string.monster_passive_perception_template,
                null),
                data.senses?.passivePerception ?: stringProvider(R.string.not_available, null)) // Обработка null для senses и passivePerception
        ))

        // Languages
        // data.languages теперь nullable
        items.add(MonsterDetailItem.CollapsibleText(R.string.monster_languages_title,
            data.languages ?: stringProvider(R.string.not_available, null))) // Обработка null для languages

        // Challenge Rating & XP - Эти поля не были изменены на nullable в Monster.kt
        items.add(MonsterDetailItem.CollapsibleText(
            stringResId = R.string.monster_challenge_rating_title,
            content = String.format(stringProvider(R.string.monster_challenge_rating_xp_template, null),
                data.challengeRating, data.xp)
        ))

        // Special Abilities
        // data.specialAbilities теперь nullable
        if (data.specialAbilities?.isNotEmpty() == true) { // Безопасная проверка на null и пустоту
            items.add(MonsterDetailItem.Header(stringProvider(
                R.string.monster_special_abilities_header, null)))

            data.specialAbilities.forEach { ability ->
                // creatureSpellcasting теперь nullable
                if (ability.creatureSpellcasting != null) {
                    // Все вложенные списки и карты теперь тоже nullable
                    val cantrips = ability.creatureSpellcasting.spells
                        ?.filter { it.level == 0 }?.joinToString { it.name }
                        ?: stringProvider(R.string.not_available, null)

                    val spellSlots = ability.creatureSpellcasting.slots?.map { (level, count) ->
                        level.toInt() to count
                    }?.sortedBy { it.first } ?: emptyList() // Если null, пустой список

                    val spellsByLevel = ability.creatureSpellcasting.spells
                        ?.filter { it.level > 0 }
                        ?.groupBy { it.level }
                        ?.mapValues { (_, spells) -> spells.map { it.name }.sorted() }
                        ?.toSortedMap() ?: sortedMapOf() // Если null, пустая карта

                    items.add(MonsterDetailItem.Spellcasting(
                        level = ability.creatureSpellcasting.level,
                        ability = ability.creatureSpellcasting.ability.name,
                        dc = ability.creatureSpellcasting.dc,
                        modifier = ability.creatureSpellcasting.modifier,
                        cantrips = cantrips,
                        spellSlots = spellSlots,
                        spellsByLevel = spellsByLevel
                    ))
                } else {
                    items.add(MonsterDetailItem.CollapsibleText(
                        stringResId = R.string.monster_special_abilities_header,
                        content = String.format(stringProvider(R.string.special_ability_template,
                            null), ability.name, ability.desc)
                    ))
                }
            }
        }

        // Actions
        // data.actions теперь nullable
        if (data.actions?.isNotEmpty() == true) { // Безопасная проверка на null и пустоту
            items.add(MonsterDetailItem
                .Header(stringProvider(R.string.monster_actions_header, null)))
            data.actions.forEach { action ->
                // action.damage теперь nullable, а также все вложенные поля
                val damageInfoText = action
                    .damage?.firstOrNull()?.from?.options?.joinToString("\n") { option ->
                        String.format(
                            stringProvider(R.string.damage_option_template, null),
                            option.damageDice ?: stringProvider(R.string.not_available, null),
                            option.damageType?.name ?: stringProvider(R.string.not_available, null),
                            option.notes ?: stringProvider(R.string.not_available, null))
                    } ?: stringProvider(R.string.not_available, null) // Значение по умолчанию, если damageInfoText null

                items.add(MonsterDetailItem.ActionItem(
                    name = action.name,
                    description = action.desc,
                    attackBonus = action.attackBonus, // attackBonus уже nullable
                    damageInfo = damageInfoText
                ))
            }
        }

        // Legendary Actions - уже был ?.let, но добавил isNotEmpty() для ясности
        data.legendaryActions?.let {
            if (it.isNotEmpty()) {
                items.add(MonsterDetailItem.Header(
                    stringProvider(R.string.monster_legendary_actions_header, null)))
                it.forEach { lAction ->
                    items.add(MonsterDetailItem.CollapsibleText(
                        stringResId = R.string.monster_legendary_actions_header,
                        content = String.format(
                            stringProvider(R.string.legendary_action_template, null),
                            lAction.name, lAction.desc)
                    ))
                }
            }
        }

        // Reactions - уже был ?.let, но добавил isNotEmpty() для ясности
        data.reactions?.let {
            if (it.isNotEmpty()) {
                items.add(MonsterDetailItem.Header(
                    stringProvider(R.string.monster_reactions_header, null)))
                it.forEach { reaction ->
                    items.add(MonsterDetailItem.CollapsibleText(
                        stringResId = R.string.monster_reactions_header,
                        content = String.format(stringProvider(R.string.reaction_template, null),
                            reaction.name, reaction.desc)
                    ))
                }
            }
        }

        return items
    }
}