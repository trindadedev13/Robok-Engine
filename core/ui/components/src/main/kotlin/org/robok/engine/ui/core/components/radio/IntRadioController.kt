package org.robok.engine.ui.core.components.radio

/*
 *  This file is part of Robok © 2024.
 *
 *  Robok is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Robok is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Robok.  If not, see <https://www.gnu.org/licenses/>.
 */

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.robok.engine.ui.core.components.preferences.base.PreferenceTemplate

@Composable
fun IntRadioController(
  default: Int,
  options: List<Int>,
  excludedOptions: List<Int> = emptyList(),
  labelFactory: (Int) -> String = { it.toString() },
  onChoiceSelected: (Int) -> Unit,
) {
  var selectedChoice by remember { mutableStateOf(default) }

  Column {
    options
      .filterNot { it in excludedOptions }
      .forEach { option ->
        PreferenceTemplate(
          title = { Text(text = labelFactory(option)) },
          modifier =
            Modifier.clickable {
              selectedChoice = option
              onChoiceSelected(option)
            },
          startWidget = { RadioButton(selected = option == selectedChoice, onClick = null) },
        )
      }
  }
}
