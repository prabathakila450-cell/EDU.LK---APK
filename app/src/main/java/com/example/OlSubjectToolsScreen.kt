package com.example

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OlSubjectToolsScreen(
  initialSubject: String = "විද්‍යාව",
  onBack: () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  var selectedSubject by remember { mutableStateOf(initialSubject) }
  var selectedGroupNumber by remember { mutableStateOf(0) } // 0: All groups
  var searchQuery by remember { mutableStateOf("") }
  var expandedToolId by remember { mutableStateOf<String?>(null) }

  val subjects = remember { OlSubjectToolsRepository.subjectsList }
  val currentSubjectCategory = remember(selectedSubject) {
    subjects.find { it.subjectName == selectedSubject } ?: subjects.first()
  }

  val allToolsForSubject = remember(selectedSubject) {
    OlSubjectToolsRepository.getToolsForSubject(selectedSubject)
  }

  val filteredTools = remember(allToolsForSubject, selectedGroupNumber, searchQuery) {
    allToolsForSubject.filter { item ->
      val matchesGroup = selectedGroupNumber == 0 || item.groupNumber == selectedGroupNumber
      val matchesSearch = searchQuery.isBlank() ||
          item.titleSinhala.contains(searchQuery, ignoreCase = true) ||
          item.titleEnglish.contains(searchQuery, ignoreCase = true) ||
          item.category.contains(searchQuery, ignoreCase = true) ||
          item.formulaOrRule.contains(searchQuery, ignoreCase = true) ||
          item.quickSummary.contains(searchQuery, ignoreCase = true)
      matchesGroup && matchesSearch
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "🛠️ O/L විෂය මෙවලම් (Tools 100)",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "${currentSubjectCategory.subjectName} • මෙවලම් 100 (10x10 කාණ්ඩ)",
              fontSize = 11.sp,
              color = Color(0xFF38BDF8)
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
      )
    },
    containerColor = Color(0xFF0B132B)
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      // 1. Subject Selector Tabs
      ScrollableTabRow(
        selectedTabIndex = subjects.indexOfFirst { it.subjectName == selectedSubject }.coerceAtLeast(0),
        containerColor = Color(0xFF0F172A),
        contentColor = Color.White,
        edgePadding = 12.dp,
        divider = { HorizontalDivider(color = Color(0xFF334155)) }
      ) {
        subjects.forEach { category ->
          val isSelected = selectedSubject == category.subjectName
          Tab(
            selected = isSelected,
            onClick = {
              selectedSubject = category.subjectName
              selectedGroupNumber = 0
              expandedToolId = null
            },
            text = {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
              ) {
                Text(category.icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "${category.subjectName} (100)",
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) category.color else Color(0xFF94A3B8)
                )
              }
            }
          )
        }
      }

      // 2. Search & Overview Header
      Surface(
        color = Color(0xFF1E293B),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
              Text("මෙවලම, සූත්‍රය හෝ නම සොයන්න...", fontSize = 12.sp, color = Color(0xFF64748B))
            },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF38BDF8), modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
              if (searchQuery.isNotBlank()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
                }
              }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color(0xFF0F172A),
              unfocusedContainerColor = Color(0xFF0F172A),
              focusedBorderColor = Color(0xFF38BDF8),
              unfocusedBorderColor = Color(0xFF334155),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          // 3. 10 Groups Filter Bar (10x10)
          Text(
            text = "📂 කාණ්ඩ 10 තෝරන්න (10x10 කාණ්ඩ ක්‍රමය):",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(bottom = 4.dp)
          )

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            item {
              FilterChip(
                selected = selectedGroupNumber == 0,
                onClick = { selectedGroupNumber = 0 },
                label = { Text("සියල්ල (100)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = Color(0xFF38BDF8),
                  selectedLabelColor = Color(0xFF0F172A),
                  containerColor = Color(0xFF0F172A),
                  labelColor = Color.White
                )
              )
            }

            items((1..10).toList()) { grp ->
              val groupName = OlSubjectToolsRepository.getGroupName(selectedSubject, grp)
              FilterChip(
                selected = selectedGroupNumber == grp,
                onClick = { selectedGroupNumber = grp },
                label = {
                  Text(
                    text = "කාණ්ඩය $grp (10)",
                    fontSize = 11.sp
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = Color(0xFF38BDF8),
                  selectedLabelColor = Color(0xFF0F172A),
                  containerColor = Color(0xFF0F172A),
                  labelColor = Color.White
                )
              )
            }
          }
        }
      }

      // Group Header Info
      if (selectedGroupNumber in 1..10) {
        Surface(
          color = Color(0xFF0F172A),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("📌", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "කාණ්ඩය $selectedGroupNumber: ${OlSubjectToolsRepository.getGroupName(selectedSubject, selectedGroupNumber)}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF38BDF8),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      // 4. Tools List (100 items per subject)
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
      ) {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "පෙන්වන්නේ මෙවලම් ${filteredTools.size} / 100",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF64748B)
            )
            Text(
              text = "100% O/L විෂය නිර්දේශානුකූලයි",
              fontSize = 10.sp,
              color = Color(0xFF10B981),
              fontWeight = FontWeight.Bold
            )
          }
        }

        items(filteredTools, key = { it.id }) { tool ->
          val isExpanded = expandedToolId == tool.id

          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(
              1.dp,
              if (isExpanded) Color(0xFF38BDF8) else Color(0xFF334155)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .animateContentSize()
              .clickable {
                expandedToolId = if (isExpanded) null else tool.id
              }
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
              ) {
                // Tool Number badge
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F172A)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "#${tool.toolNumber}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF38BDF8)
                  )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(tool.icon, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = tool.titleSinhala,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                    )
                  }
                  Text(
                    text = "${tool.titleEnglish} • [${tool.category}]",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color(0xFF334155)
                ) {
                  Text(
                    text = "G${tool.groupNumber}",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                  imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                  contentDescription = if (isExpanded) "Collapse" else "Expand",
                  tint = Color(0xFF94A3B8),
                  modifier = Modifier.size(20.dp)
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              // Quick summary
              Text(
                text = tool.quickSummary,
                fontSize = 11.sp,
                color = Color(0xFFE2E8F0),
                lineHeight = 16.sp
              )

              // Formula or Rule highlight bar
              Spacer(modifier = Modifier.height(6.dp))
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0F172A),
                border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = "⚡ සූත්‍රය / නීතිය / මූලධර්මය:",
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF38BDF8)
                    )
                    Text(
                      text = tool.formulaOrRule,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFFF1F5F9)
                    )
                  }
                  IconButton(
                    onClick = {
                      clipboardManager.setText(AnnotatedString(tool.formulaOrRule))
                      Toast.makeText(context, "සූත්‍රය පිටපත් විය (Copied)!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.ContentCopy,
                      contentDescription = "Copy Formula",
                      tint = Color(0xFF94A3B8),
                      modifier = Modifier.size(15.dp)
                    )
                  }
                }
              }

              // Expanded Details (Practical App & Exam Tip)
              AnimatedVisibility(visible = isExpanded) {
                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                ) {
                  HorizontalDivider(color = Color(0xFF334155), modifier = Modifier.padding(vertical = 4.dp))

                  // Practical Application
                  Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("🛠️ ", fontSize = 11.sp)
                    Column {
                      Text(
                        text = "ප්‍රායෝගික භාවිතය:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                      )
                      Text(
                        text = tool.practicalApplication,
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1)
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(4.dp))

                  // Exam Tip
                  Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("🎯 ", fontSize = 11.sp)
                    Column {
                      Text(
                        text = "O/L විභාග රහස & ලකුණු ලබාගැනීමේ Tip:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B)
                      )
                      Text(
                        text = tool.examTip,
                        fontSize = 11.sp,
                        color = Color(0xFFFEF3C7)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
