package com.example

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class SubjectSpecialInfo(
  val section: SpecialFeatureSection,
  val title: String,
  val subtitle: String,
  val emoji: String,
  val color: Color
)

data class GradeCardInfo(
  val grade: String,
  val gradeSinhala: String,
  val subtitle: String,
  val description: String,
  val imageRes: Int,
  val color: Color,
  val tag: String
)

val availableGradesList = listOf(
  GradeCardInfo(
    grade = "10",
    gradeSinhala = "10 ශ්‍රේණිය (Grade 10)",
    subtitle = "සාමාන්‍ය පෙළ මූලික අධ්‍යයන",
    description = "විද්‍යාව, ගණිතය, සිංහල, ඉංග්‍රීසි, කෘෂි, වාණිජ්‍ය, ICT හා කෙටි සටහන්",
    imageRes = R.drawable.img_subjects_bg_1786107319789,
    color = Color(0xFF0D47A1),
    tag = "O/L ආරම්භය"
  ),
  GradeCardInfo(
    grade = "11",
    gradeSinhala = "11 ශ්‍රේණිය (Grade 11)",
    subtitle = "අ.පො.ස. සාමාන්‍ය පෙළ (O/L)",
    description = "විද්‍යාව, ගණිතය, ඉතිහාසය, සිංහල, ඉංග්‍රීසි, ICT හා පසුගිය ප්‍රශ්න පත්‍ර",
    imageRes = R.drawable.img_portal_header_bg_1786107362621,
    color = Color(0xFF1B5E20),
    tag = "O/L විභාගය"
  )
)

fun getSubjectBgImage(subjectNameSinhala: String): Int {
  return when {
    subjectNameSinhala.contains("භූගෝල") || subjectNameSinhala.contains("ඉතිහාසය") || subjectNameSinhala.contains("බුද්ධ") || subjectNameSinhala.contains("සිංහල") || subjectNameSinhala.contains("පුරවැසි") -> R.drawable.img_notes_bg_1786107335103
    (subjectNameSinhala.contains("විද්‍යාව") && !subjectNameSinhala.contains("භූගෝල")) || subjectNameSinhala.contains("Science") -> R.drawable.img_subjects_bg_1786107319789
    subjectNameSinhala.contains("ගණිතය") || subjectNameSinhala.contains("Math") -> R.drawable.img_papers_bg_1786107349533
    subjectNameSinhala.contains("තාක්ෂණ") || subjectNameSinhala.contains("ICT") || subjectNameSinhala.contains("වීඩියෝ") -> R.drawable.img_videos_bg_1786110404209
    subjectNameSinhala.contains("සංගීත") || subjectNameSinhala.contains("නර්තන") || subjectNameSinhala.contains("Music") -> R.drawable.img_subjects_bg_1786107319789
    else -> R.drawable.img_portal_header_bg_1786107362621
  }
}

/**
 * STEP 1: Grade Selection Screen (Home Screen)
 * Displays Grades 11 down to 06 in descending order with rich educational background photos
 */
@Composable
fun GradesHomeScreen(
  onGradeSelected: (String) -> Unit,
  onOpenAddModal: () -> Unit,
  onOpenAnalytics: () -> Unit = {},
  onOpenStructuredEssay: () -> Unit = {},
  onOpenFormulaHandbook: () -> Unit = {},
  onOpenVoiceQuiz: () -> Unit = {},
  onOpenEnglishBuilder: () -> Unit = {},
  onOpenSyllabusHub: () -> Unit = {},
  onOpenCountdownPlanner: () -> Unit = {},
  onOpenFlashcards: () -> Unit = {},
  onOpenStudyStreak: () -> Unit = {},
  onOpenSpokenEnglishVoice: () -> Unit = {},
  onOpenBookmarks: () -> Unit = {},
  onOpenMockExam: () -> Unit = {},
  onOpenSpotTopics: () -> Unit = {},
  onOpenLiveDailyQuiz: () -> Unit = {},
  onOpenEyeCare: () -> Unit = {},
  onOpenHistoryMapsAutoCheck: () -> Unit = {},
  onOpenScience500AutoCheck: () -> Unit = {},
  onOpenEnglishShortNotesAutoCheck: () -> Unit = {},
  onOpenGeographyAutoCheck: () -> Unit = {},
  onOpenSpecialMasterFeatures: (SpecialFeatureSection?) -> Unit = {},
  onOpenGrade10TermTestPortal: (Grade10WebSource) -> Unit = {},
  onOpenFormulaSolver: () -> Unit = {},
  onOpenTriLingualGlossary: () -> Unit = {},
  onOpenTimedPastPaperSimulator: () -> Unit = {},
  onOpenOfflineNotesVault: () -> Unit = {},
  onOpenInteractiveQaHub: (QaHubTab) -> Unit = {},
  onOpenVoiceDoubtSolver: () -> Unit = {},
  onOpenMistakeNotebook: () -> Unit = {},
  isFeatureTrialActive: Boolean = true,
  isApproved: Boolean = false,
  remainingTrialHours: Int = 24
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 10.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // 🎓 10 ශ්‍රේණිය සහ 11 ශ්‍රේණිය අනු කොටස් (ඇප් එකේ ඉහළටම ස්ථානගත කර ඇත)
    val grade10Info = availableGradesList.firstOrNull { it.grade == "10" }
    val grade11Info = availableGradesList.firstOrNull { it.grade == "11" }

    grade10Info?.let {
      GradeBannerCard(
        gradeInfo = it,
        onClick = { onGradeSelected(it.grade) }
      )
    }

    grade11Info?.let {
      GradeBannerCard(
        gradeInfo = it,
        onClick = { onGradeSelected(it.grade) }
      )
    }

    // 🏆 FEATURE: DAILY AUTOMATED LIVE 7:00 PM QUIZ CONTEST BANNER (Compact)
    Card(
      shape = RoundedCornerShape(10.dp),
      colors = CardDefaults.cardColors(containerColor = Color.Transparent),
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(
          Brush.horizontalGradient(
            colors = listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF4338CA))
          )
        )
        .clickable(onClick = onOpenLiveDailyQuiz)
        .testTag("live_daily_quiz_entry_card")
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color(0xFFF59E0B)),
          contentAlignment = Alignment.Center
        ) {
          Text("🏆", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "⚡ සජීවී දෛනික Quiz",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color.White,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color(0xFFEF4444)
            ) {
              Text(
                text = "LIVE",
                fontSize = 7.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                maxLines = 1
              )
            }
          }

          Text(
            text = "Grades 9-11 • ප්‍රශ්න 20 • Leaderboard",
            fontSize = 9.sp,
            color = Color(0xFFC7D2FE),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Icon(
          Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = "Open",
          tint = Color.White,
          modifier = Modifier.size(14.dp)
        )
      }
    }
    // Top Smart Study Tools Quick Strip (විශේෂාංග කලාපය)
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Section Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⚡", fontSize = 15.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = "විශේෂාංග කලාපය (Features Hub)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                fontSize = 13.5.sp
              )
              Text(
                text = "සියලුම ස්මාර්ට් අධ්‍යයන මෙවලම් සහ කෙටි ක්‍රම",
                fontSize = 9.5.sp,
                color = Color(0xFF64748B)
              )
            }
          }
        }

        // NEW FEATURE: 🎙️ AI Voice Doubt Solver Hero Banner
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF091E3A)),
          border = BorderStroke(1.2.dp, Color(0xFF38BDF8)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOpenVoiceDoubtSolver() }
            .testTag("home_voice_doubt_solver_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    Color(0xFF0F172A),
                    Color(0xFF1E3A8A),
                    Color(0xFF0284C7)
                  )
                )
              )
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFEF4444)),
                contentAlignment = Alignment.Center
              ) {
                Text("🎙️", fontSize = 20.sp)
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "AI ශිෂ්‍ය ගැටලු හඬ සහායකයා",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF10B981)
                  ) {
                    Text(
                      text = "VOICE EXPLAIN",
                      fontSize = 8.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = Color.White,
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                  }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "ඕනෑම ගැටලුවක් විමසූ සැනින් සම්පූර්ණ හඬ පැහැදිලි කිරීම (Full Voice)",
                  fontSize = 10.sp,
                  color = Color(0xFFBAE6FD),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }
            Icon(
              Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Open",
              tint = Color(0xFF38BDF8),
              modifier = Modifier.size(18.dp)
            )
          }
        }

        // Feature 1: Standout English Master Class Hero Banner
        EnglishHeroFeatureCard(
          onClick = onOpenEnglishBuilder
        )

        // Feature 2: Ultra-Compact Mistake Notebook Indicator (වරදවාගත් ප්‍රශ්න - Minimized width, completely non-obtrusive)
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color(0xFF0F172A).copy(alpha = 0.95f),
          border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.35f)),
          shadowElevation = 0.5.dp,
          modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .widthIn(max = 195.dp)
            .clickable { onOpenMistakeNotebook() }
        ) {
          Row(
            modifier = Modifier
              .padding(horizontal = 8.dp, vertical = 3.5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text("⚠️", fontSize = 10.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "වරදවාගත් ප්‍රශ්න",
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              color = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFDC2626)
            ) {
              Text(
                text = "200",
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Open",
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(11.dp)
            )
          }
        }

        // Row 1: Flashcards & Bookmarks Hub
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FeatureSubCardWithBg(
            title = "1. Flashcards",
            subtitle = "මතක කාඩ්පත් (300)",
            emoji = "💡",
            badgeText = "CARDS",
            badgeColor = Color(0xFF16A34A),
            modifier = Modifier.weight(1f),
            onClick = onOpenFlashcards
          )

          FeatureSubCardWithBg(
            title = "2. Bookmarks Hub",
            subtitle = "සුරැකි සටහන්",
            emoji = "🔖",
            badgeText = "SAVED",
            badgeColor = Color(0xFF4F46E5),
            modifier = Modifier.weight(1f),
            onClick = onOpenBookmarks
          )
        }

        // Row 2: AI Voice Quiz & Analytics
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FeatureSubCardWithBg(
            title = "3. AI Voice Quiz",
            subtitle = "කටහඬින් ප්‍රශ්න & Viva",
            emoji = "🎙️",
            badgeText = "VOICE AI",
            badgeColor = Color(0xFFDC2626),
            modifier = Modifier.weight(1f),
            onClick = onOpenVoiceQuiz
          )

          FeatureSubCardWithBg(
            title = "4. ප්‍රගති වාර්තා",
            subtitle = "Analytics & Stats",
            emoji = "📊",
            badgeText = "STATS",
            badgeColor = Color(0xFF2563EB),
            modifier = Modifier.weight(1f),
            onClick = onOpenAnalytics
          )
        }

        // Row 3: Structured Essays & Formulas/Timelines
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FeatureSubCardWithBg(
            title = "5. ව්‍යුහගත/රචනා",
            subtitle = "Marking Schemes",
            emoji = "📝",
            badgeText = "ESSAY",
            badgeColor = Color(0xFF9333EA),
            modifier = Modifier.weight(1f),
            onClick = onOpenStructuredEssay
          )

          FeatureSubCardWithBg(
            title = "6. සූත්‍ර & Timeline",
            subtitle = "Handbook & Rules",
            emoji = "🧮",
            badgeText = "FORMULA",
            badgeColor = Color(0xFF059669),
            modifier = Modifier.weight(1f),
            onClick = onOpenFormulaHandbook
          )
        }

        // Row 4: Live Mock Exam Hall & Spot Topics
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FeatureSubCardWithBg(
            title = "7. සජීවී Exam Hall",
            subtitle = "OMR Simulator",
            emoji = "🏛️",
            badgeText = "EXAM",
            badgeColor = Color(0xFFDC2626),
            modifier = Modifier.weight(1f),
            onClick = onOpenMockExam
          )

          FeatureSubCardWithBg(
            title = "8. Spot Topics",
            subtitle = "අනුමාන මාතෘකා",
            emoji = "🎯",
            badgeText = "TOPICS",
            badgeColor = Color(0xFF0284C7),
            modifier = Modifier.weight(1f),
            onClick = onOpenSpotTopics
          )
        }

        // Row 5: O/L Special Master Interactive Suite & Formula Solver
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FeatureSubCardWithBg(
            title = "9. O/L මෙවලම්",
            subtitle = "සියලු විෂයන් Tools",
            emoji = "🌟",
            badgeText = "TOOLS",
            badgeColor = Color(0xFF8B5CF6),
            modifier = Modifier.weight(1f),
            onClick = { onOpenSpecialMasterFeatures(null) }
          )

          FeatureSubCardWithBg(
            title = "10. සූත්‍ර විසඳුම්කරු",
            subtitle = "පියවරෙන් පියවර Solver",
            emoji = "🧮",
            badgeText = "SOLVER",
            badgeColor = Color(0xFF0284C7),
            modifier = Modifier.weight(1f),
            onClick = onOpenFormulaSolver
          )
        }

        // Row 6: Trilingual Glossary & Past Paper Simulator
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FeatureSubCardWithBg(
            title = "11. ත්‍රිභාෂා ශබ්දකෝෂය",
            subtitle = "විද්‍යාව/ගණිතය/ICT",
            emoji = "📖",
            badgeText = "GLOSSARY",
            badgeColor = Color(0xFF0D9488),
            modifier = Modifier.weight(1f),
            onClick = onOpenTriLingualGlossary
          )

          FeatureSubCardWithBg(
            title = "12. Past Paper Simulator",
            subtitle = "වසර අනුව Timed Exam",
            emoji = "⏱️",
            badgeText = "EXAM",
            badgeColor = Color(0xFFE11D48),
            modifier = Modifier.weight(1f),
            onClick = onOpenTimedPastPaperSimulator
          )
        }

        // Row 7: Interactive Q&A Suite - Short Answer & Daily 10 Challenge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FeatureSubCardWithBg(
            title = "13. කෙටි පිළිතුරු ඇගයීම",
            subtitle = "Marking Scheme පරීක්ෂාව",
            emoji = "✍️",
            badgeText = "Q&A SUITE",
            badgeColor = Color(0xFF38BDF8),
            modifier = Modifier.weight(1f),
            onClick = { onOpenInteractiveQaHub(QaHubTab.SHORT_ANSWER) }
          )

          FeatureSubCardWithBg(
            title = "14. දිනපතා මිශ්‍ර ප්‍රශ්න 10",
            subtitle = "Streak & Score පරීක්ෂණය",
            emoji = "🎯",
            badgeText = "DAILY 10",
            badgeColor = Color(0xFF10B981),
            modifier = Modifier.weight(1f),
            onClick = { onOpenInteractiveQaHub(QaHubTab.DAILY_CHALLENGE) }
          )
        }

        // Row 8: Match the Following / Timeline & True/False Justifications
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FeatureSubCardWithBg(
            title = "15. ගැලපීම් & කාලරේඛා",
            subtitle = "Matching & Timeline",
            emoji = "🧩",
            badgeText = "MATCHING",
            badgeColor = Color(0xFF8B5CF6),
            modifier = Modifier.weight(1f),
            onClick = { onOpenInteractiveQaHub(QaHubTab.MATCHING) }
          )

          FeatureSubCardWithBg(
            title = "16. 09/10/11 සත්‍ය/අසත්‍ය & Quiz",
            subtitle = "විෂයකට ප්‍රශ්න 500 • කාණ්ඩ 30",
            emoji = "⚖️",
            badgeText = "500 Qs",
            badgeColor = Color(0xFFEC4899),
            modifier = Modifier.weight(1f),
            onClick = { onOpenInteractiveQaHub(QaHubTab.TRUE_FALSE) }
          )
        }
      }
    }

    // SECTION REMOVED AS REQUESTED: සාමාන්‍ය පෙළ ශ්‍රේණි අනුව විෂයන් (10 සහ 11 ශ්‍රේණි)
    Spacer(modifier = Modifier.height(10.dp))
  }
}

@Composable
fun EnglishHeroFeatureCard(
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    border = BorderStroke(1.2.dp, Color(0xFFF59E0B)),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            colors = listOf(
              Color(0xFF0F172A),
              Color(0xFF1E1B4B),
              Color(0xFF312E81)
            )
          )
        )
        .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(Color(0xFFF59E0B).copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Text("🇬🇧", fontSize = 15.sp)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "ඉංග්‍රීසි පන්තිය (English Hub)",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFDE047)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Surface(
                shape = RoundedCornerShape(3.dp),
                color = Color(0xFFF59E0B)
              ) {
                Text(
                  text = "09-11 ALL",
                  fontSize = 7.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF78350F),
                  modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                )
              }
            }
            Text(
              text = "කෙටි සටහන්, ව්‍යාකරණ, ස්පෝකන් ඉංග්‍රීසි & සම්පූර්ණ ඩික්ෂනරිය",
              fontSize = 9.sp,
              color = Color(0xFFE2E8F0),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
        Icon(
          imageVector = Icons.Default.ArrowForwardIos,
          contentDescription = "Open",
          tint = Color(0xFFFDE047),
          modifier = Modifier.size(13.dp)
        )
      }
    }
  }
}

@Composable
fun FeatureSubCardWithBg(
  title: String,
  subtitle: String,
  emoji: String,
  badgeText: String? = null,
  badgeColor: Color = Color(0xFFF59E0B),
  backgroundImageRes: Int = 0,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val (cardStartColor, cardEndColor) = when (badgeText) {
    "PLANNER" -> Color(0xFF1E1B4B) to Color(0xFF312E81)
    "CARDS" -> Color(0xFF064E3B) to Color(0xFF047857)
    "REWARDS" -> Color(0xFF78350F) to Color(0xFFB45309)
    "SAVED" -> Color(0xFF1E1B4B) to Color(0xFF4338CA)
    "VOICE AI" -> Color(0xFF7F1D1D) to Color(0xFFB91C1C)
    "STATS" -> Color(0xFF1E3A8A) to Color(0xFF2563EB)
    "ESSAY" -> Color(0xFF581C87) to Color(0xFF7E22CE)
    "FORMULA" -> Color(0xFF064E3B) to Color(0xFF059669)
    "EXAM" -> Color(0xFF881337) to Color(0xFFBE123C)
    "TOPICS" -> Color(0xFF0C4A6E) to Color(0xFF0284C7)
    "NIGHT" -> Color(0xFF0F172A) to Color(0xFF334155)
    "MAPS" -> Color(0xFF0B192C) to Color(0xFF1E3E62)
    "SCIENCE" -> Color(0xFF064E3B) to Color(0xFF047857)
    "ENGLISH" -> Color(0xFF0F172A) to Color(0xFF0284C7)
    "GEO" -> Color(0xFF064E3B) to Color(0xFF047857)
    "TOOLS" -> Color(0xFF312E81) to Color(0xFF4C1D95)
    "PAPERS" -> Color(0xFF022C22) to Color(0xFF064E3B)
    "SYLLABUS" -> Color(0xFF1E1B4B) to Color(0xFF4338CA)
    else -> Color(0xFF0F172A) to Color(0xFF1E293B)
  }

  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.35f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .background(
          Brush.verticalGradient(
            colors = listOf(
              cardStartColor,
              cardEndColor
            )
          )
        )
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(emoji, fontSize = 13.sp)
          if (badgeText != null) {
            Surface(
              shape = RoundedCornerShape(3.dp),
              color = badgeColor
            ) {
              Text(
                text = badgeText,
                fontSize = 7.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                maxLines = 1
              )
            }
          }
        }
        Column {
          Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = subtitle,
            fontSize = 8.sp,
            color = Color(0xFFCBD5E1),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}

@Composable
fun GradeBannerCard(
  gradeInfo: GradeCardInfo,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    border = BorderStroke(1.dp, gradeInfo.color.copy(alpha = 0.4f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("grade_card_${gradeInfo.grade}")
      .clickable { onClick() }
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            colors = listOf(
              Color(0xFF0F172A),
              gradeInfo.color.copy(alpha = 0.85f),
              gradeInfo.color
            )
          )
        )
        .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = gradeInfo.color
            ) {
              Text(
                text = gradeInfo.tag,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = gradeInfo.gradeSinhala,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${gradeInfo.subtitle} • ${gradeInfo.description}",
            fontSize = 9.5.sp,
            color = Color.White.copy(alpha = 0.9f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Surface(
          shape = CircleShape,
          color = Color.White.copy(alpha = 0.25f)
        ) {
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Open Grade",
            tint = Color.White,
            modifier = Modifier
              .padding(5.dp)
              .size(14.dp)
          )
        }
      }
    }
  }
}

/**
 * STEP 2: Subject Selection Screen for a Chosen Grade
 * Displays list of subjects for that grade with individual background photos
 */
@Composable
fun GradeSubjectsScreen(
  grade: String,
  subjectsList: SnapshotStateList<SubjectItem>,
  isAdmin: Boolean = false,
  onSubjectSelected: (SubjectItem) -> Unit,
  onBackToGrades: () -> Unit,
  onAddSubject: () -> Unit,
  onDeleteSubject: (SubjectItem) -> Unit,
  onOpenGrade10TermTestPortal: (Grade10WebSource) -> Unit = {},
  onOpenGrade11TermTestPortal: (Grade11WebSource, String?, Int?) -> Unit = { _, _, _ -> }
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Navigation bar back button + Grade title
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = BluePrimaryContainer,
      border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onBackToGrades,
            modifier = Modifier.testTag("back_to_grades_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Grades",
              tint = BlueOnPrimaryContainer
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Column {
            Text(
              text = "$grade ශ්‍රේණිය • විෂයන් (Subjects)",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = BlueOnPrimaryContainer
            )
            Text(
              text = "විෂයයක් තෝරා කෙටි සටහන්, ප්‍රශ්න පත්‍ර හා වීඩියෝ බලන්න",
              style = MaterialTheme.typography.bodySmall,
              color = BlueOnPrimaryContainer.copy(alpha = 0.8f),
              fontSize = 11.sp
            )
          }
        }

        if (isAdmin) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = BluePrimary,
            onClick = onAddSubject
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Subject",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("නව විෂයක්", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 🛡️ 10 ශ්‍රේණිය වාර විභාග ප්‍රශ්න පත්‍ර (Grade 10 Papers Sub-Component at the very top of Grade 10)
    if (grade == "10") {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.2.dp, Color(0xFFDC2626)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("grade_10_papers_portal_card")
          .clickable { onOpenGrade10TermTestPortal(Grade10WebSource.GOVDOC) }
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  Color(0xFF0F172A),
                  Color(0xFF450A0A),
                  Color(0xFF7F1D1D)
                )
              )
            )
            .padding(horizontal = 12.dp, vertical = 9.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDC2626).copy(alpha = 0.25f)),
                  contentAlignment = Alignment.Center
                ) {
                  Text("📑", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "10 වසර වාර විභාග ප්‍රශ්න පත්‍ර",
                      fontSize = 12.5.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFFECACA)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      shape = RoundedCornerShape(4.dp),
                      color = Color(0xFFDC2626)
                    ) {
                      Text(
                        text = "PAPERS PORTAL",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = "GovDoc.lk & e-Thaksalawa සියලුම පළාත් වාර විභාග ප්‍රශ්න පත්‍ර",
                    fontSize = 9.sp,
                    color = Color(0xFFE2E8F0),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
              Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Open Grade 10 Papers",
                tint = Color(0xFFFECACA),
                modifier = Modifier.size(13.dp)
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { onOpenGrade10TermTestPortal(Grade10WebSource.GOVDOC) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text("GovDoc Papers", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
              }
              OutlinedButton(
                onClick = { onOpenGrade10TermTestPortal(Grade10WebSource.ETHAKSALAWA) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFECACA)),
                border = BorderStroke(1.dp, Color(0xFFFECACA).copy(alpha = 0.5f)),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text("e-Thaksalawa", fontSize = 10.sp, color = Color(0xFFFECACA))
              }
            }
          }
        }
      }
    }

    // 🛡️ 11 ශ්‍රේණිය වාර විභාග ප්‍රශ්න පත්‍ර (Grade 11 Papers Sub-Component at the very top of Grade 11)
    if (grade == "11") {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.2.dp, Color(0xFF059669)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("grade_11_papers_portal_card")
          .clickable { onOpenGrade11TermTestPortal(Grade11WebSource.GOVDOC, null, null) }
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  Color(0xFF0F172A),
                  Color(0xFF064E3B),
                  Color(0xFF047857)
                )
              )
            )
            .padding(horizontal = 12.dp, vertical = 9.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF10B981).copy(alpha = 0.25f)),
                  contentAlignment = Alignment.Center
                ) {
                  Text("📑", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "11 වසර වාර විභාග ප්‍රශ්න පත්‍ර",
                      fontSize = 12.5.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFA7F3D0)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      shape = RoundedCornerShape(4.dp),
                      color = Color(0xFF059669)
                    ) {
                      Text(
                        text = "PAPERS PORTAL",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = "GovDoc.lk & e-Thaksalawa (1, 2, 3 වාර හා විෂය අනුව පමණි)",
                    fontSize = 9.sp,
                    color = Color(0xFFE2E8F0),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
              Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Open Grade 11 Papers",
                tint = Color(0xFFA7F3D0),
                modifier = Modifier.size(13.dp)
              )
            }

            // Quick Portal Access Buttons & Term Filter Shortcuts
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Button(
                onClick = { onOpenGrade11TermTestPortal(Grade11WebSource.GOVDOC, null, null) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp),
                modifier = Modifier.weight(1.1f)
              ) {
                Text("GovDoc Papers", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
              }
              OutlinedButton(
                onClick = { onOpenGrade11TermTestPortal(Grade11WebSource.ETHAKSALAWA, null, null) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFA7F3D0)),
                border = BorderStroke(1.dp, Color(0xFFA7F3D0).copy(alpha = 0.6f)),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text("e-Thaksalawa", fontSize = 10.sp, color = Color(0xFFA7F3D0))
              }
            }

            // Quick Term Filter Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              listOf(1 to "1 වන වාරය", 2 to "2 වන වාරය", 3 to "3 වන වාරය").forEach { (termNum, termText) ->
                Surface(
                  onClick = { onOpenGrade11TermTestPortal(Grade11WebSource.GOVDOC, null, termNum) },
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFF064E3B),
                  border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f)),
                  modifier = Modifier.weight(1f)
                ) {
                  Text(
                    text = termText,
                    fontSize = 9.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 3.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    if (subjectsList.isEmpty()) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceVariantLight,
        border = BorderStroke(1.dp, NeutralBorderLight),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Default.MenuBook,
            contentDescription = "Empty Subjects",
            tint = BluePrimary,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "මෙම ශ්‍රේණිය සඳහා විෂයන් නොමැත",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = NeutralDark
          )
          Text(
            text = "නව විෂයක් එක් කිරීමට ඉහත 'නව විෂයක්' බොත්තම ඔබන්න.",
            fontSize = 12.sp,
            color = NeutralMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )
        }
      }
    } else {
      subjectsList.forEach { subject ->
        SubjectPhotoCard(
          subject = subject,
          grade = grade,
          onClick = { onSubjectSelected(subject) }
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))
  }
}

@Composable
fun SubjectPhotoCard(
  subject: SubjectItem,
  grade: String,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    border = BorderStroke(1.dp, subject.color.copy(alpha = 0.35f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("subject_card_${subject.id}")
      .clickable { onClick() }
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            colors = listOf(
              Color(0xFF0F172A),
              Color(0xFF1E293B),
              subject.color.copy(alpha = 0.55f)
            )
          )
        )
        .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(subject.color),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = subject.icon,
            contentDescription = subject.name,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = subject.nameSinhala,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${subject.name} • $grade ශ්‍රේණිය",
            fontSize = 11.sp,
            color = Color(0xFFCBD5E1)
          )
        }

        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = "Open",
          tint = Color(0xFF94A3B8),
          modifier = Modifier.size(16.dp)
        )
      }
    }
  }
}

fun normalizeSubjectKey(name: String): String {
  val lower = name.lowercase().trim()
  return when {
    lower.contains("භූගෝල") || lower.contains("geography") -> "geography"
    lower.contains("පුරවැසි") || lower.contains("civic") -> "civic"
    lower.contains("නර්තන") || lower.contains("dance") || lower.contains("dancing") -> "dance"
    lower.contains("සංගීත") || lower.contains("music") -> "music"
    lower.contains("චිත්‍ර") || lower.contains("art") -> "art"
    lower.contains("නාට්‍ය") || lower.contains("drama") -> "drama"
    lower.contains("කෘෂි") || lower.contains("ආහාර") || lower.contains("agri") -> "agri"
    lower.contains("සෞඛ්‍ය") || lower.contains("ශාරීරික") || lower.contains("health") -> "health"
    lower.contains("තොරතුරු") || lower.contains("තාක්ෂණ") || lower.contains("ict") -> "ict"
    lower.contains("ව්‍යාපාර") || lower.contains("ගිණුම්") || lower.contains("commerce") || lower.contains("accounting") -> "commerce"
    (lower.contains("විද්‍යා") && !lower.contains("භූගෝල")) || lower.contains("science") -> "science"
    lower.contains("ගණිත") || lower.contains("math") -> "math"
    lower.contains("ඉතිහාස") || lower.contains("history") -> "history"
    lower.contains("බුද්ධ") || lower.contains("buddhism") -> "buddhism"
    lower.contains("සිංහල") || lower.contains("sinhala") -> "sinhala"
    lower.contains("ඉංග්‍රීසි") || lower.contains("english") -> "english"
    else -> lower
  }
}

fun isMatchSubject(itemSubject: String, itemTitle: String, targetSubject: SubjectItem): Boolean {
  val targetKey = normalizeSubjectKey(targetSubject.nameSinhala + " " + targetSubject.name)
  val itemKey = normalizeSubjectKey(itemSubject + " " + itemTitle)
  
  if (targetKey == "geography" && (itemKey != "geography" || (!itemSubject.contains("භූගෝල") && !itemTitle.contains("භූගෝල")))) {
    return false
  }
  if (targetKey == "science" && (itemKey == "geography" || itemSubject.contains("භූගෝල") || itemTitle.contains("භූගෝල"))) {
    return false
  }
  if (targetKey == itemKey) return true

  return itemSubject.equals(targetSubject.nameSinhala, ignoreCase = true) ||
      (itemSubject.contains(targetSubject.nameSinhala, ignoreCase = true) && !targetSubject.nameSinhala.contains("විද්‍යාව") && !itemSubject.contains("භූගෝල"))
}

/**
 * STEP 3: Content Tabs Screen for a Selected Subject
 * Shows 3 Tabs:
 * Tab 0: කෙටි සටහන් (Short Notes - PDFs)
 * Tab 1: ප්‍රශ්න පත්‍ර (Question Papers - PDFs)
 * Tab 2: කෙටි සටහන් වීඩියෝස් (Video Lessons)
 */
@Composable
fun SubjectContentScreen(
  grade: String,
  subject: SubjectItem,
  selectedTab: Int,
  isAdmin: Boolean = false,
  onTabSelected: (Int) -> Unit,
  notesList: List<ShortNoteItem>,
  papersList: List<QuestionPaperItem>,
  videosList: List<VideoLessonItem>,
  onPdfClick: (title: String, pdfUri: String?, isPasswordProtected: Boolean, password: String?) -> Unit,
  onDeleteNote: (ShortNoteItem) -> Unit,
  onDeletePaper: (QuestionPaperItem) -> Unit,
  onDeleteVideo: (VideoLessonItem) -> Unit,
  onVideoClick: (VideoLessonItem) -> Unit,
  onBackToSubjects: () -> Unit,
  onAddContent: (String) -> Unit,
  onStartQuizSet: (QuizSet) -> Unit = {},
  onOpenFlashcards: () -> Unit = {},
  activeAudio: ActiveAudioState? = null,
  onPlayAudio: (ChapterItem, speed: Float) -> Unit = { _, _ -> },
  onPauseAudio: () -> Unit = {},
  onResumeAudio: () -> Unit = {},
  onOpenPdfAtPage: (pdfUrl: String, title: String, page: Int) -> Unit = { _, _, _ -> },
  onOpenAnalytics: () -> Unit = {},
  onOpenStructuredEssay: () -> Unit = {},
  onOpenFormulaHandbook: () -> Unit = {},
  onOpenVoiceQuiz: () -> Unit = {},
  onOpenEnglishBuilder: () -> Unit = {},
  onOpenSyllabusHub: () -> Unit = {},
  onOpenHistoryMapsAutoCheck: () -> Unit = {},
  onOpenScience500AutoCheck: () -> Unit = {},
  onOpenEnglishShortNotesAutoCheck: () -> Unit = {},
  onOpenGeographyAutoCheck: () -> Unit = {},
  onOpenMathShortNotesAutoCheck: () -> Unit = {},
  onOpenSpecialMasterFeatures: (SpecialFeatureSection?) -> Unit = {},
  onOpenGrade10TermTestPortal: (Grade10WebSource) -> Unit = {},
  onOpenGrade11TermTestPortal: (Grade11WebSource, String?, Int?) -> Unit = { _, _, _ -> },
  onOpenInteractiveQaHub: (QaHubTab) -> Unit = {},
  onOpenVoiceDoubtSolver: () -> Unit = {},
  initialSearchQuery: String = ""
) {
  var showDeleteConfirmDialog by remember { mutableStateOf(false) }
  var itemToDeleteTitle by remember { mutableStateOf("") }
  var pendingDeleteAction by remember { mutableStateOf<(() -> Unit)?>(null) }
  var noteSearchFilter by remember(initialSearchQuery) { mutableStateOf(initialSearchQuery) }

  val subjectNotes = remember(notesList, subject.id, noteSearchFilter) {
    notesList.filter { isMatchSubject(it.subject, it.topicSinhala, subject) }
      .filter { note ->
        if (noteSearchFilter.isBlank()) true
        else note.title.contains(noteSearchFilter, ignoreCase = true) ||
             note.topicSinhala.contains(noteSearchFilter, ignoreCase = true) ||
             note.fileName?.contains(noteSearchFilter, ignoreCase = true) == true ||
             note.subject.contains(noteSearchFilter, ignoreCase = true)
      }
  }
  val subjectPapers = remember(papersList, subject.id) {
    papersList.filter { isMatchSubject(it.subject, it.titleSinhala, subject) }
  }
  val activePdfUrl = remember(subjectNotes) {
    subjectNotes.firstOrNull()?.pdfUri ?: "https://drive.google.com/file/d/1V3y65z_15X6zjruQ_I11WhG4EOfDHGm-/preview"
  }

  // Confirmation Dialog for Admin PDF/Note/Paper Deletion
  if (showDeleteConfirmDialog) {
    AlertDialog(
      onDismissRequest = {
        showDeleteConfirmDialog = false
        pendingDeleteAction = null
      },
      icon = {
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color(0xFFFEE2E2)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.DeleteForever,
            contentDescription = "Delete",
            tint = Color(0xFFDC2626),
            modifier = Modifier.size(28.dp)
          )
        }
      },
      title = {
        Text(
          text = "PDF ගොනුව ඉවත් කිරීම (Delete)",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = Color(0xFFDC2626)
        )
      },
      text = {
        Column {
          Text(
            text = "මෙම PDF ගොනුව / සටහන ස්ථිරවම ඉවත් කිරීමට ඔබට අවශ්‍යද?",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NeutralDark
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "ගොනුව: $itemToDeleteTitle",
            fontSize = 12.sp,
            color = NeutralMedium
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "මෙය සිදුකළ පසු ඇප් එක භාවිතා කරන සියලුම පරිශීලකයින්ටද මෙම PDF ගොනුව නොපෙනී යනු ඇත.",
            fontSize = 11.sp,
            color = Color(0xFFB91C1C)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            pendingDeleteAction?.invoke()
            showDeleteConfirmDialog = false
            pendingDeleteAction = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("ඔව්, ඉවත් කරන්න (Yes)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      },
      dismissButton = {
        OutlinedButton(
          onClick = {
            showDeleteConfirmDialog = false
            pendingDeleteAction = null
          },
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("නැත (No)", color = NeutralDark, fontSize = 12.sp)
        }
      }
    )
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Subject Banner Header with Back Button
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, NeutralBorderLight),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(115.dp)
      ) {
        Image(
          painter = painterResource(id = getSubjectBgImage(subject.nameSinhala)),
          contentDescription = subject.nameSinhala,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.85f),
                  Color.Black.copy(alpha = 0.35f)
                )
              )
            )
        )

        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Surface(
              shape = CircleShape,
              color = Color.White.copy(alpha = 0.25f),
              onClick = onBackToSubjects,
              modifier = Modifier.testTag("back_to_subjects_btn")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                  .padding(8.dp)
                  .size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = subject.color
              ) {
                Text(
                  text = "$grade ශ්‍රේණිය",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = subject.nameSinhala,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "${subject.name} • 5-Section Hub",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }

    // Quick Tool Strip inside Subject
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Surface(
        onClick = onOpenAnalytics,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFEFF6FF),
        border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
        modifier = Modifier.weight(1f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text("📊", fontSize = 11.sp)
          Spacer(modifier = Modifier.width(2.dp))
          Text("ප්‍රගතිය", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D4ED8), maxLines = 1)
        }
      }

      Surface(
        onClick = onOpenStructuredEssay,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFAF5FF),
        border = BorderStroke(1.dp, Color(0xFFE9D5FF)),
        modifier = Modifier.weight(1f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text("📝", fontSize = 11.sp)
          Spacer(modifier = Modifier.width(2.dp))
          Text("රචනා", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7E22CE), maxLines = 1)
        }
      }

      Surface(
        onClick = onOpenFormulaHandbook,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF0FDF4),
        border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
        modifier = Modifier.weight(1f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text("🧮", fontSize = 11.sp)
          Spacer(modifier = Modifier.width(2.dp))
          Text("සූත්‍ර", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D), maxLines = 1)
        }
      }

      Surface(
        onClick = onOpenVoiceQuiz,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFEF2F2),
        border = BorderStroke(1.dp, Color(0xFFFECACA)),
        modifier = Modifier.weight(1f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text("🎙️", fontSize = 11.sp)
          Spacer(modifier = Modifier.width(2.dp))
          Text("Viva", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB91C1C), maxLines = 1)
        }
      }

      Surface(
        onClick = onOpenEnglishBuilder,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFFFBEB),
        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
        modifier = Modifier.weight(1f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text("🔤", fontSize = 11.sp)
          Spacer(modifier = Modifier.width(2.dp))
          Text("English", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309), maxLines = 1)
        }
      }
    }

    // 5-Section Subject Hub: 1. කෙටි සටහන් | 2. ප්‍රශ්න පත්‍ර | 3. ස්වයං පුහුණු | 4. 🎧 Audio | 5. ⚡ AI Assistant
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = SurfaceVariantLight,
      border = BorderStroke(1.dp, NeutralBorderLight),
      modifier = Modifier.fillMaxWidth()
    ) {
      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.Transparent,
        contentColor = BluePrimary,
        edgePadding = 8.dp,
        indicator = { tabPositions ->
          if (selectedTab < tabPositions.size) {
            TabRowDefaults.SecondaryIndicator(
              Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = if (selectedTab >= 3) Color(0xFF7C3AED) else BluePrimary,
              height = 3.dp
            )
          }
        }
      ) {
        val tabTitles = listOf(
          "කෙටි සටහන්",
          "ප්‍රශ්න පත්‍ර",
          "ස්වයං පුහුණු",
          "🎧 Audio Notes",
          "⚡ AI Assistant"
        )
        val tabIcons = listOf(
          Icons.Default.Description,
          Icons.Default.Quiz,
          Icons.Default.MenuBook,
          Icons.Default.Headphones,
          Icons.Default.AutoAwesome
        )

        tabTitles.forEachIndexed { index, title ->
          val isSelected = selectedTab == index
          val isAiHighlighted = index >= 3

          Tab(
            selected = isSelected,
            onClick = { onTabSelected(index) },
            text = {
              Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = when {
                  isSelected && isAiHighlighted -> Color(0xFF7C3AED)
                  isSelected -> BluePrimary
                  isAiHighlighted -> Color(0xFF6D28D9)
                  else -> NeutralMedium
                },
                maxLines = 1
              )
            },
            icon = {
              Icon(
                imageVector = tabIcons[index],
                contentDescription = title,
                tint = when {
                  isSelected && isAiHighlighted -> Color(0xFF7C3AED)
                  isSelected -> BluePrimary
                  isAiHighlighted -> Color(0xFF6D28D9)
                  else -> NeutralMedium
                },
                modifier = Modifier.size(17.dp)
              )
            },
            modifier = Modifier.testTag("content_tab_$index")
          )
        }
      }
    }

    // 🌟 DEDICATED SUBJECT SPECIAL MASTER TOOL BAR (Diagrams, Timeline, Maps, Ledger, Writing Vault)
    val specInfo = remember(subject.nameSinhala) {
      when {
        subject.nameSinhala.contains("විද්‍යාව") && !subject.nameSinhala.contains("භූගෝල") ->
          SubjectSpecialInfo(SpecialFeatureSection.SCIENCE_DIAGRAMS, "විද්‍යාව රූප සටහන් නම් කිරීම & සූත්‍ර බැංකුව", "Human Heart, Eye, Cells & Formulas Lab", "🔬", Color(0xFF0D9488))
        subject.nameSinhala.contains("ඉංග්‍රීසි") || subject.name.contains("English") ->
          SubjectSpecialInfo(SpecialFeatureSection.ENGLISH_WRITING_VAULT, "O/L English Writing Vault & Irregular Verbs", "Notices, Formal Letters, Charts & Verbs", "✍️", Color(0xFF0284C7))
        subject.nameSinhala.contains("ඉතිහාසය") ->
          SubjectSpecialInfo(SpecialFeatureSection.HISTORY_TIMELINE_MAPS, "ඓතිහාසික කාල රේඛාව සහ සිතියම් සලකුණු", "Prehistoric to Modern Eras & Map Practice", "🏛️", Color(0xFFD97706))
        subject.nameSinhala.contains("ගණිත") || subject.name.contains("Math", ignoreCase = true) ->
          SubjectSpecialInfo(SpecialFeatureSection.OL_100_SUBJECT_TOOLS, "10 & 11 ගණිතය කෙටි සටහන් (ඒකක 42)", "Sandu Theory 42 Units, Formulas & Solutions", "📐", Color(0xFF0284C7))
        subject.nameSinhala.contains("භූගෝල") ->
          SubjectSpecialInfo(SpecialFeatureSection.GEOGRAPHY_TOPOMAPS, "1:50,000 භූ ලක්ෂණ සිතියම් & ලෝක කලාප", "Contour Lines, Drainage & World Regions", "🗺️", Color(0xFF059669))
        subject.nameSinhala.contains("ව්‍යාපාර") || subject.nameSinhala.contains("ගිණුම්") || subject.nameSinhala.contains("වාණිජ") ->
          SubjectSpecialInfo(SpecialFeatureSection.COMMERCE_DOUBLE_ENTRY, "ද්විත්ව සටහන් සහ මූල්‍ය ප්‍රකාශන Sandbox", "Double Entry Rules, Statements & Ledger", "📊", Color(0xFF7C3AED))
        subject.nameSinhala.contains("නර්තන") || subject.nameSinhala.contains("සංගීත") ->
          SubjectSpecialInfo(SpecialFeatureSection.DANCING_TRADITIONS, "ත්‍රිවිධ දේශීය නර්තන සම්ප්‍රදාය & වන්නම් 18", "Kandyan, Low-country, Sabaragamuwa & Vannam", "💃", Color(0xFFE11D48))
        subject.nameSinhala.contains("සිංහල") ->
          SubjectSpecialInfo(SpecialFeatureSection.SINHALA_LITERATURE, "සිංහල සාහිත්‍යය රසාස්වාද සැලසුම් & පද්‍ය විවරණ", "Poetry Analysis, Drama, Prose & Grammar Rules", "📜", Color(0xFFE11D48))
        subject.nameSinhala.contains("බුද්ධ") || subject.nameSinhala.contains("ආගම") ->
          SubjectSpecialInfo(SpecialFeatureSection.BUDDHISM_DHAMMA, "බුද්ධ ධර්මය සූත්‍ර විශ්ලේෂණ & ශාසන ඉතිහාසය", "Key Suttas, Sasana History & Dhamma Quiz", "☸️", Color(0xFFF59E0B))
        else ->
          SubjectSpecialInfo(SpecialFeatureSection.OFFICIAL_MARKING_SCHEME, "O/L නිල Marking Schemes & විභාග මගපෙන්වීම", "Department Marking Criteria & Evaluation", "📋", Color(0xFF2563EB))
      }
    }

    Surface(
      onClick = {
        if (subject.nameSinhala.contains("ගණිත") || subject.name.contains("Math", ignoreCase = true)) {
          onOpenMathShortNotesAutoCheck()
        } else {
          onOpenSpecialMasterFeatures(specInfo.section)
        }
      },
      shape = RoundedCornerShape(10.dp),
      color = Color(0xFF0F172A),
      border = BorderStroke(1.2.dp, specInfo.color),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("subject_special_master_tool_bar")
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(specInfo.color.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Text(specInfo.emoji, fontSize = 14.sp)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = specInfo.title,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(5.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = specInfo.color
              ) {
                Text(
                  text = "INTERACTIVE",
                  fontSize = 7.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
            }
            Text(
              text = specInfo.subtitle,
              fontSize = 9.sp,
              color = Color(0xFF94A3B8)
            )
          }
        }

        Surface(
          shape = CircleShape,
          color = specInfo.color
        ) {
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Open Tool",
            tint = Color.White,
            modifier = Modifier
              .padding(4.dp)
              .size(13.dp)
          )
        }
      }
    }

    // SECTION 1 (Tab 0): කෙටි සටහන් (Short Notes - PDFs)
    if (selectedTab == 0) {
      if (noteSearchFilter.isNotBlank()) {
        Card(
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
          border = BorderStroke(1.dp, Color(0xFF93C5FD)),
          modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
              Icon(Icons.Default.FilterList, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "🎯 ඉලක්කගත සටහන් පෙරහන: \"$noteSearchFilter\"",
                fontSize = 11.5.sp,
                color = Color(0xFF1E3A8A),
                fontWeight = FontWeight.Bold
              )
            }
            TextButton(
              onClick = { noteSearchFilter = "" },
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
              Text("සියල්ල (Clear)", fontSize = 10.5.sp, color = BluePrimary, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "කෙටි සටහන් හා PDF ලේඛන (${subjectNotes.size})",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = NeutralDark
        )

        if (isAdmin) {
          Button(
            onClick = { onAddContent("NOTE") },
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("PDF එකක් එක් කරන්න", fontSize = 11.sp)
          }
        }
      }

      // History Blank Maps & 100% Auto-Checker Banner for History Subject
      if (subject.nameSinhala.contains("ඉතිහාසය") || subject.id.contains("history")) {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onOpenHistoryMapsAutoCheck() }
            .testTag("history_maps_subject_banner")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🗺️", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "09/10/11 ඉතිහාසය සිතියම් Auto-Checker",
                  color = Color.White,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Surface(
                color = Color(0xFF0284C7),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "100% CHECK",
                  color = Color.White,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "හිස්තැන් සහිත සිතියම් හඳුනාගෙන ලකුණු කරන්න • ස්වයංක්‍රීයව ලකුණු ලබාගන්න සහ වැරදි නිවැරදි කරගන්න.",
              color = Color(0xFFBAE6FD),
              fontSize = 12.sp,
              lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = onOpenHistoryMapsAutoCheck,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text("සිතියම් පරීක්ෂාව (Auto-Check)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
              OutlinedButton(
                onClick = {
                  onPdfClick(
                    "09, 10 සහ 11 ශ්‍රේණි - ඉතිහාසය හිස්තැන් සහිත සිතියම් පුහුණුව",
                    HistoryMapsRepository.pdfDriveUrl,
                    false,
                    null
                  )
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8)),
                border = BorderStroke(1.dp, Color(0xFF38BDF8)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text("හිස්තැන් PDF බලන්න", fontSize = 11.sp)
              }
            }
          }
        }
      }

      // Science 500 Questions Interactive Auto-Checker Banner for Science Subject
      if (subject.nameSinhala.contains("විද්‍යාව") || subject.id.contains("science")) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onOpenScience500AutoCheck() }
            .testTag("science_500_subject_banner")
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
              ) {
                Text("🧬", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "11 විද්‍යාව ප්‍රශ්න 500 Auto-Checker",
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = Color(0xFF10B981),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "100% නිවැරදි",
                  color = Color.White,
                  fontSize = 8.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  maxLines = 1
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "ප්‍රශ්න 500 සඳහා එවෙලෙම පිළිතුරු සපයන්න • ස්වයංක්‍රීයව ලකුණු • සවිස්තරාත්මක විවරණ.",
              color = Color(0xFFA7F3D0),
              fontSize = 11.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = onOpenScience500AutoCheck,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("ප්‍රශ්න 500 පරීක්ෂාව", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
              }
              OutlinedButton(
                onClick = {
                  onPdfClick(
                    "11 ශ්‍රේණිය - විද්‍යාව ප්‍රශ්න 500 විශේෂ කෙටි සටහන් සහ ප්‍රශ්නෝත්තර සංග්‍රහය (PDF)",
                    Science500Repository.pdfDriveUrl,
                    false,
                    null
                  )
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6EE7B7)),
                border = BorderStroke(1.dp, Color(0xFF6EE7B7)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("PDF බලන්න", fontSize = 11.sp)
              }
            }
          }
        }
      }

      // English Short Notes & Grammar Auto-Checker Banner for English Subject
      if (subject.nameSinhala.contains("ඉංග්‍රීසි") || subject.name.contains("English", ignoreCase = true) || subject.id.contains("english")) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onOpenEnglishShortNotesAutoCheck() }
            .testTag("english_short_notes_subject_banner")
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
              ) {
                Text("🔤", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "09/10/11 ඉංග්‍රීසි කෙටි සටහන් Auto-Checker",
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = Color(0xFF38BDF8),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "100% ACCURATE",
                  color = Color(0xFF0F172A),
                  fontSize = 8.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  maxLines = 1
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "ප්‍රශ්නවලට එවෙලෙම පිළිතුරු • ස්වයංක්‍රීයව ලකුණු • සිංහල විවරණ සහ Grammar Rules.",
              color = Color(0xFFBAE6FD),
              fontSize = 11.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = onOpenEnglishShortNotesAutoCheck,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("ප්‍රශ්න පරීක්ෂාව (Auto-Check)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
              }
            }
          }
        }
      }

      // Geography Short Notes Interactive Auto-Checker Banner for Geography Subject
      if (subject.nameSinhala.contains("භූගෝල") || subject.name.contains("Geography", ignoreCase = true) || subject.id.contains("geo")) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onOpenGeographyAutoCheck() }
            .testTag("geography_short_notes_subject_banner")
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
              ) {
                Text("🌍", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "10/11 භූගෝලය කෙටි සටහන් Auto-Checker",
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = Color(0xFF10B981),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "100% නිවැරදි",
                  color = Color.White,
                  fontSize = 8.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  maxLines = 1
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "ප්‍රශ්නවලට එවෙලෙම පිළිතුරු • ස්වයංක්‍රීයව ලකුණු • සවිස්තර විවරණ සහ විභාග ඉඟි.",
              color = Color(0xFFA7F3D0),
              fontSize = 11.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = onOpenGeographyAutoCheck,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("ප්‍රශ්න පරීක්ෂාව (Auto-Check)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
              }
              OutlinedButton(
                onClick = {
                  onPdfClick(
                    "10 සහ 11 ශ්‍රේණි - භූගෝල විද්‍යාව කෙටි සටහන් සහ ප්‍රශ්නෝත්තර සංග්‍රහය (PDF)",
                    GeographyAutoCheckerRepository.pdfDriveUrl,
                    false,
                    null
                  )
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6EE7B7)),
                border = BorderStroke(1.dp, Color(0xFF6EE7B7)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("මුල් PDF බලන්න", fontSize = 11.sp)
              }
            }
          }
        }
      }

      // Grade 10 & 11 Mathematics Short Notes (Sandu Theory 42 Units) Banner for Maths Subject
      if (subject.nameSinhala.contains("ගණිත") || subject.name.contains("Math", ignoreCase = true) || subject.id.contains("math")) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onOpenMathShortNotesAutoCheck() }
            .testTag("math_short_notes_subject_banner")
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
              ) {
                Text("📐", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "10 & 11 ගණිතය කෙටි සටහන් (ඒකක 42)",
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = Color(0xFF38BDF8),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "SANDU THEORY",
                  color = Color(0xFF0F172A),
                  fontSize = 8.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  maxLines = 1
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "සියලු සූත්‍ර • පියවරෙන් පියවර විසඳූ ගැටලු • 1-42 සියලු ඒකක වල නිල පිළිතුරු & සූත්‍ර ගණක.",
              color = Color(0xFFBAE6FD),
              fontSize = 11.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = onOpenMathShortNotesAutoCheck,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("කෙටි සටහන් & විසඳුම් විවරණ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
              }
              OutlinedButton(
                onClick = {
                  onPdfClick(
                    "10 හා 11 ශ්‍රේණි ගණිතය කෙටි සටහන් (Sandu Theory ඒකක 42)",
                    "https://drive.google.com/file/d/1V3y65z_15X6zjruQ_I11WhG4EOfDHGm-/preview",
                    false,
                    null
                  )
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8)),
                border = BorderStroke(1.dp, Color(0xFF38BDF8)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("PDF (පිටු 100)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      if (subject.nameSinhala.contains("සිංහල")) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
          border = BorderStroke(1.2.dp, Color(0xFFE11D48).copy(alpha = 0.6f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("📜", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "10/11 සිංහල සාහිත්‍යය & රසාස්වාද සැලසුම් Hub",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFDA4AF)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "සැළලිහිණි සන්දේශය, ගුත්තිලය, යශෝධරාවත පද්‍ය විවරණ, මනමේ-සිංහබාහු නාට්‍ය සහ වියරණ නීති සම්පූර්ණ එකතුව.",
              fontSize = 11.sp,
              color = Color(0xFFCBD5E1)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = { onOpenSpecialMasterFeatures(SpecialFeatureSection.SINHALA_LITERATURE) },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
              Text("සාහිත්‍යය Hub විවෘත කරන්න 🚀", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
          }
        }
      }

      if (subject.nameSinhala.contains("බුද්ධ") || subject.nameSinhala.contains("ආගම")) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
          border = BorderStroke(1.2.dp, Color(0xFFF59E0B).copy(alpha = 0.6f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("☸️", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "10/11 බුද්ධ ධර්මය සූත්‍ර & ශාසන ඉතිහාස Hub",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFDE68A)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "මංගල, පරාභව, ධම්මචක්කප්පවත්තන සූත්‍ර විවරණ, ධර්ම සංගායනා 3, මහින්දාගමනය සහ සිව්සස් ප්‍රශ්නෝත්තර.",
              fontSize = 11.sp,
              color = Color(0xFFCBD5E1)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = { onOpenSpecialMasterFeatures(SpecialFeatureSection.BUDDHISM_DHAMMA) },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
              Text("බුද්ධ ධර්මය Hub විවෘත කරන්න 🚀", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
          }
        }
      }

      // 🎯 Interactive Q&A Suite Bar for the subject (Ultra-Slim & Compact)
      Surface(
        onClick = { onOpenInteractiveQaHub(QaHubTab.SHORT_ANSWER) },
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF0F172A),
        border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.6f)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("interactive_qa_subject_bar")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(Color(0xFF0284C7).copy(alpha = 0.25f)),
              contentAlignment = Alignment.Center
            ) {
              Text("🎯", fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "${subject.nameSinhala} • Q&A Hub",
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFBAE6FD),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(5.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFF0284C7)
                ) {
                  Text(
                    text = "6 MODES",
                    color = Color.White,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
              Text(
                text = "කෙටි පිළිතුරු • Flashcards • Daily 10 • ගැලපීම්",
                fontSize = 8.5.sp,
                color = Color(0xFF94A3B8),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFF0284C7),
              onClick = { onOpenInteractiveQaHub(QaHubTab.SHORT_ANSWER) }
            ) {
              Text(
                text = "කෙටි පිළිතුරු ✍️",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFD97706),
              onClick = { onOpenInteractiveQaHub(QaHubTab.FLASHCARDS) }
            ) {
              Text(
                text = "⚡",
                fontSize = 10.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }
        }
      }

      if (subjectNotes.isEmpty()) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = SurfaceVariantLight,
          border = BorderStroke(1.dp, NeutralBorderLight),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.PictureAsPdf,
              contentDescription = "PDF Empty",
              tint = Color(0xFFC62828),
              modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "${subject.nameSinhala} සඳහා කෙටි සටහන් නොමැත",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = NeutralDark
            )
            if (isAdmin) {
              Text(
                text = "ඔබ සතු PDF එකතු කිරීමට 'PDF එකක් එක් කරන්න' බොත්තම ඔබන්න.",
                fontSize = 11.sp,
                color = NeutralMedium,
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      } else {
        subjectNotes.forEach { note ->
          ShortNoteCardRow(
            note = note,
            isAdmin = isAdmin,
            onClick = {
              onPdfClick(note.topicSinhala, note.pdfUri, note.isPasswordProtected, note.password)
            },
            onDelete = if (isAdmin) {
              {
                itemToDeleteTitle = note.topicSinhala
                pendingDeleteAction = { onDeleteNote(note) }
                showDeleteConfirmDialog = true
              }
            } else null,
            onOpenPdf = {
              onPdfClick(note.topicSinhala, note.pdfUri, note.isPasswordProtected, note.password)
            },
            onOpenQuiz = {
              val quizData = QuizRepository.getQuizDataForSubject(grade, subject.nameSinhala, note.pdfUri ?: note.id)
              onStartQuizSet(quizData.quiz_sets.first())
            },
            onOpenFlashcards = {
              onOpenFlashcards()
            }
          )
        }
      }
    }

    // SECTION 2 (Tab 1): ප්‍රශ්න පත්‍ර (G.C.E. O/L Papers & Term Test Papers & Custom Papers)
    if (selectedTab == 1) {
      // 1. GRADE 11 SPECIAL: G.C.E. O/L 6-Year Exam Papers & Marking Schemes (2024 මෙවර සිට 2019 දක්වා)
      if (grade == "11") {
        GceOlPastPapersSection(
          subject = subject,
          onOpenPdf = { title, url ->
            onPdfClick(title, url, false, null)
          }
        )

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 2. SUB-SECTION: වාර විභාග ප්‍රශ්න පත්‍ර (Term Test Evaluation Papers - 1st, 2nd, 3rd Terms across 5 Years)
      TermTestPapersSection(
        grade = grade,
        subject = subject,
        onOpenPdf = { title, url ->
          onPdfClick(title, url, false, null)
        },
        onOpenGrade10Portal = onOpenGrade10TermTestPortal,
        onOpenGrade11Portal = onOpenGrade11TermTestPortal
      )

      Spacer(modifier = Modifier.height(14.dp))

      // 2. SUB-SECTION: අතිරේක හා ගුරුභවතුන් එක් කළ ප්‍රශ්න පත්‍ර (Custom/Community Added Papers)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "අතිරේක හා ආදර්ශ ප්‍රශ්න පත්‍ර (${subjectPapers.size})",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = NeutralDark
          )
          Text(
            text = "අභිරුචි PDF ප්‍රශ්න පත්‍ර හා සාකච්ඡා සටහන්",
            fontSize = 10.sp,
            color = NeutralMedium
          )
        }

        if (isAdmin) {
          Button(
            onClick = { onAddContent("PAPER") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF137333)),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("PDF එකක් එක් කරන්න", fontSize = 11.sp)
          }
        }
      }

      if (subjectPapers.isNotEmpty()) {
        subjectPapers.forEach { paper ->
          QuestionPaperCardRow(
            paper = paper,
            isAdmin = isAdmin,
            onClick = {
              onPdfClick(paper.titleSinhala, paper.pdfUri, paper.isPasswordProtected, paper.password)
            },
            onDelete = if (isAdmin) {
              {
                itemToDeleteTitle = paper.titleSinhala
                pendingDeleteAction = { onDeletePaper(paper) }
                showDeleteConfirmDialog = true
              }
            } else null,
            onOpenPdf = {
              onPdfClick(paper.titleSinhala, paper.pdfUri, paper.isPasswordProtected, paper.password)
            }
          )
        }
      }
    }

    // SECTION 3 (Tab 2): ස්වයං පුහුණු (Practice Quizzes & Flashcards)
    if (selectedTab == 2) {
      PracticeQuizzesSection(
        grade = grade,
        subject = subject,
        onStartQuizSet = onStartQuizSet,
        onOpenFlashcards = onOpenFlashcards
      )
    }

    // SECTION 4 (Tab 3): 🎧 Audio Notes & Podcasts (AI Powered)
    if (selectedTab == 3) {
      AudioNotesSection(
        grade = grade,
        subject = subject,
        pdfUrl = activePdfUrl,
        activeAudio = activeAudio,
        onPlayAudio = onPlayAudio,
        onPauseAudio = onPauseAudio,
        onResumeAudio = onResumeAudio,
        onOpenPdfAtPage = onOpenPdfAtPage
      )
    }

    // SECTION 5 (Tab 4): ⚡ AI Smart Assistant & Voice Helper
    if (selectedTab == 4) {
      // 🎙️ VOICE DOUBT SOLVER CALLOUT BANNER
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        border = BorderStroke(1.2.dp, Color(0xFFEF4444)),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onOpenVoiceDoubtSolver() }
          .testTag("subject_voice_doubt_banner")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFEF4444)),
              contentAlignment = Alignment.Center
            ) {
              Text("🎙️", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "🎙️ ගැටලු විමසූ විට සම්පූර්ණ හඬ පැහැදිලි කිරීම",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "ශිෂ්‍ය ගැටලු සඳහා Sinhala Voice Explainer වෙත පිවිසෙන්න",
                fontSize = 9.5.sp,
                color = Color(0xFFFCA5A5)
              )
            }
          }
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Open", tint = Color(0xFFF87171), modifier = Modifier.size(18.dp))
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      AiSmartAssistantSection(
        grade = grade,
        subject = subject,
        pdfUrl = activePdfUrl,
        onOpenPdfAtPage = onOpenPdfAtPage
      )
    }

    Spacer(modifier = Modifier.height(10.dp))
  }
}
