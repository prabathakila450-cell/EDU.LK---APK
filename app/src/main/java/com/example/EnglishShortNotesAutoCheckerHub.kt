package com.example

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

enum class EnglishGradeLevel(val label: String, val badgeColor: Color) {
  ALL("සියලු ශ්‍රේණි (10, 11)", Color(0xFF1E293B)),
  GRADE_11("11 ශ්‍රේණිය (O/L)", Color(0xFF047857)),
  GRADE_10("10 ශ්‍රේණිය", Color(0xFF1D4ED8))
}

enum class EnglishSkillCategory(
  val displayName: String,
  val iconEmoji: String,
  val primaryColor: Color,
  val bgLightColor: Color
) {
  ALL("සියලු මාතෘකා", "📚", Color(0xFF1E3A8A), Color(0xFFEFF6FF)),
  TENSES("Tenses & Verb Forms", "⏳", Color(0xFF0284C7), Color(0xFFF0F9FF)),
  PASSIVE_VOICE("Active & Passive Voice", "🔄", Color(0xFF059669), Color(0xFFF0FDF4)),
  REPORTED_SPEECH("Direct & Indirect Speech", "💬", Color(0xFFD97706), Color(0xFFFFFBEB)),
  CONDITIONALS("If Clauses & Conditionals", "🔀", Color(0xFF7C3AED), Color(0xFFFAF5FF)),
  PREPOSITIONS("Prepositions & Phrasal Verbs", "📍", Color(0xFFDB2777), Color(0xFFFDF2F8)),
  CONJUNCTIONS("Conjunctions & Connectors", "🔗", Color(0xFF4F46E5), Color(0xFFEEF2FF)),
  RELATIVE_CLAUSES("Relative Clauses (who/which/that)", "👥", Color(0xFF0D9488), Color(0xFFF0FDFA)),
  QUESTION_TAGS("Question Tags & Inversions", "❓", Color(0xFFEA580C), Color(0xFFFFF7ED)),
  VOCABULARY("Vocabulary, Idioms & Collocations", "📖", Color(0xFF2563EB), Color(0xFFEFF6FF)),
  ERROR_CORRECTION("Sentence Structure & Errors", "✍️", Color(0xFFB91C1C), Color(0xFFFEF2F2))
}

data class EnglishGrammarRuleSummary(
  val title: String,
  val category: EnglishSkillCategory,
  val formula: String,
  val explanationSinhala: String,
  val examples: List<String>,
  val commonMistake: String
)

data class EnglishShortNoteQuestionItem(
  val id: String,
  val number: Int,
  val gradeLevel: EnglishGradeLevel,
  val category: EnglishSkillCategory,
  val topicTitle: String,
  val questionPrompt: String,
  val questionSinhalaGuidance: String,
  val options: List<String>,
  val correctOptionIndex: Int,
  val explanationSinhala: String,
  val grammarRuleOrFormula: String,
  val examTip: String
)

object EnglishShortNotesRepository {

  val grammarRulesList: List<EnglishGrammarRuleSummary> = listOf(
    EnglishGrammarRuleSummary(
      title = "Present Perfect Tense",
      category = EnglishSkillCategory.TENSES,
      formula = "Subject + have/has + Past Participle (V3) + Object",
      explanationSinhala = "අතීතයේ සිදුවූ නමුත් එහි බලපෑම වර්තමානයට පවතින ක්‍රියාවන්, හෝ මේ දැන් අවසන් වූ (just / already / yet) ක්‍රියා දැක්වීමට භාවිත කරයි.",
      examples = listOf(
        "She has already finished her homework.",
        "They have lived in Kandy for ten years.",
        "Have you ever seen an elephant?"
      ),
      commonMistake = "❌ She have went -> ✅ She has gone (He/She/It සඳහා 'has' ද, V3 ක්‍රියා පදයද යෙදිය යුතුය)."
    ),
    EnglishGrammarRuleSummary(
      title = "Active to Passive Voice (Simple Present / Past)",
      category = EnglishSkillCategory.PASSIVE_VOICE,
      formula = "Object + is/am/are/was/were + V3 (Past Participle) + (by + Agent)",
      explanationSinhala = "ක්‍රියාව කළ පුද්ගලයාට (Subject) වඩා ක්‍රියාවට ලක්වූ දේට (Object) මුල්තැන දෙන විට කර්මකාරක (Passive Voice) යෙදේ.",
      examples = listOf(
        "Active: The teacher explains the lesson. -> Passive: The lesson is explained by the teacher.",
        "Active: Shakespeare wrote Hamlet. -> Passive: Hamlet was written by Shakespeare."
      ),
      commonMistake = "❌ A letter was wrote -> ✅ A letter was written (සෑම විටම Past Participle හෙවත් 3 වන ක්‍රියා පද රූපය යෙදිය යුතුය)."
    ),
    EnglishGrammarRuleSummary(
      title = "Conditionals (Type 1, 2, 3)",
      category = EnglishSkillCategory.CONDITIONALS,
      formula = "Type 1: If + Present Simple, will + V1\nType 2: If + Past Simple, would + V1\nType 3: If + Past Perfect (had + V3), would have + V3",
      explanationSinhala = "Type 1: විය හැකි සත්‍ය අවස්ථා. Type 2: මනඃකල්පිත අවස්ථා. Type 3: අතීතයේ සිදු නොවූ පසුතැවිලි වන අවස්ථා.",
      examples = listOf(
        "Type 1: If it rains, we will stay at home.",
        "Type 2: If I won the lottery, I would buy a car.",
        "Type 3: If you had studied hard, you would have passed the exam."
      ),
      commonMistake = "❌ If I will study, I will pass -> ✅ If I study, I will pass (If clause එක තුළ 'will' නොයෙදේ)."
    ),
    EnglishGrammarRuleSummary(
      title = "Reported Speech (Direct to Indirect)",
      category = EnglishSkillCategory.REPORTED_SPEECH,
      formula = "Tense Shifts: Present Simple -> Past Simple | Present Continuous -> Past Continuous | Past Simple -> Past Perfect",
      explanationSinhala = "කෙනෙකු කියූ දෙයක් වෙනත් අයෙකුට වාර්තා කිරීමේදී Reporting verb (said/told) අතීත කාලීන නම් ප්‍රකාශයද අතීතයට මාරු වේ.",
      examples = listOf(
        "Direct: 'I am tired,' Kamal said. -> Indirect: Kamal said that he was tired.",
        "Direct: 'We will come tomorrow,' they said. -> Indirect: They said that they would come the next day."
      ),
      commonMistake = "Time words change: today -> that day, tomorrow -> the following/next day, yesterday -> the previous day."
    ),
    EnglishGrammarRuleSummary(
      title = "Relative Pronouns (Who, Which, That, Whose, Whom)",
      category = EnglishSkillCategory.RELATIVE_CLAUSES,
      formula = "Who = Persons | Which = Things/Animals | That = Persons & Things | Whose = Possession | Where = Places",
      explanationSinhala = "නාම පදයකට අමතර තොරතුරු එක් කිරීමට හෝ වාක්‍ය දෙකක් එකිනෙක සම්බන්ධ කිරීමට යොදා ගනී.",
      examples = listOf(
        "The boy who won the race is my friend.",
        "The car which is parked outside belongs to my uncle.",
        "The girl whose bag was lost cried."
      ),
      commonMistake = "❌ The man which helped me -> ✅ The man who helped me (මිනිසුන් සඳහා who/that යෙදිය යුතුය)."
    ),
    EnglishGrammarRuleSummary(
      title = "Question Tags",
      category = EnglishSkillCategory.QUESTION_TAGS,
      formula = "Positive Statement + Negative Tag (isn't he? / don't they?)\nNegative Statement + Positive Tag (is he? / do they?)",
      explanationSinhala = "ප්‍රකාශයක් තහවුරු කරගැනීමට වාක්‍ය අගට එක් කරන කෙටි ප්‍රශ්නයි. ප්‍රකාශය ධන නම් ටැගය සෘණ වේ.",
      examples = listOf(
        "She is a doctor, isn't she?",
        "They don't like spicy food, do they?",
        "You played well, didn't you?",
        "Let's go for a walk, shall we?"
      ),
      commonMistake = "I am late -> tag is 'aren't I?' (not 'amn't I')."
    ),
    EnglishGrammarRuleSummary(
      title = "Conjunctions: Although / In spite of / Despite",
      category = EnglishSkillCategory.CONJUNCTIONS,
      formula = "Although + Subject + Verb | In spite of / Despite + Noun / Verb-ing",
      explanationSinhala = "පරස්පර අදහස් දැක්වීමට (Contrast) භාවිත කරයි.",
      examples = listOf(
        "Although it rained heavily, we went out.",
        "Despite the heavy rain, we went out.",
        "In spite of being ill, she attended school."
      ),
      commonMistake = "❌ Despite of the rain -> ✅ Despite the rain ('Despite' සමඟ 'of' කිසිවිටෙක නොයෙදේ)."
    )
  )

  val allQuestions: List<EnglishShortNoteQuestionItem> = listOf(
    // -------------------------------------------------------------------------
    // GRADE 11 QUESTIONS (O/L Syllabus Core Units)
    // -------------------------------------------------------------------------
    EnglishShortNoteQuestionItem(
      id = "eng_q1",
      number = 1,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.CONDITIONALS,
      topicTitle = "Conditional Sentences (Type 3)",
      questionPrompt = "If the rescue team __________ ten minutes earlier, they would have saved all the passengers.",
      questionSinhalaGuidance = "අතීතයේ සිදු නොවූ සිදුවීමක් පිළිබඳ කනගාටුව/පසුතැවීම දැක්වෙන 3 වන කොන්දේසි වාක්‍යයකි (Third Conditional).",
      options = listOf(
        "1. arrived",
        "2. had arrived",
        "3. would arrive",
        "4. has arrived"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "Main clause එකෙහි 'would have + V3' (would have saved) ඇති බැවින් If clause එක සඳහා අනිවාර්යයෙන්ම Past Perfect (had + V3 -> had arrived) යෙදිය යුතුය. මෙය Type 3 Conditional නීතියයි.",
      grammarRuleOrFormula = "If + Past Perfect (had + V3) , Subject + would have + V3",
      examTip = "විභාගයේදී 'would have' දුටු විගස If කොටසට 'had + V3' තෝරන්න."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q2",
      number = 2,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.REPORTED_SPEECH,
      topicTitle = "Direct into Indirect Speech",
      questionPrompt = "Direct: 'I have lost my identity card,' the student said.\nIndirect: The student said that he __________ his identity card.",
      questionSinhalaGuidance = "Present Perfect කාලය Indirect Speech වලට හැරවීමේදී Past Perfect බවට පත්වේ.",
      options = listOf(
        "1. has lost",
        "2. is losing",
        "3. had lost",
        "4. loses"
      ),
      correctOptionIndex = 2,
      explanationSinhala = "Reporting verb එක 'said' (Past) බැවින් Direct speech හි ඇති 'have lost' (Present Perfect) ක්‍රියා පදය Indirect speech හිදී 'had lost' (Past Perfect) බවට පරිවර්තනය වේ. සර්වනාමයද 'my' සිට 'his' බවට මාරු වේ.",
      grammarRuleOrFormula = "Direct: have/has + V3  ->  Indirect: had + V3",
      examTip = "Reporting verb එක said/told වූ විට tense එක එක් පියවරක් අතීතයට (Backshift) ගමන් කරයි."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q3",
      number = 3,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.PASSIVE_VOICE,
      topicTitle = "Passive Voice (Continuous Tense)",
      questionPrompt = "Active: The municipal council is constructing a new bridge across the river.\nPassive: A new bridge __________ across the river by the municipal council.",
      questionSinhalaGuidance = "Present Continuous වාක්‍යයක් Passive Voice බවට හැරවීම.",
      options = listOf(
        "1. is constructed",
        "2. is being constructed",
        "3. was constructed",
        "4. has been constructed"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "Present Continuous ක්‍රියාවක් (is constructing) Passive Voice බවට පත්වීමේදී 'is/are + being + V3' (is being constructed) රූපය ගනී. 'being' යෙදීම අත්‍යවශ්‍ය වේ.",
      grammarRuleOrFormula = "Present Continuous Passive = is / are + being + Past Participle (V3)",
      examTip = "Continuous ක්‍රියාවන්හි Passive වලදී 'being' අනිවාර්යයෙන් තිබිය යුතුය."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q4",
      number = 4,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.CONJUNCTIONS,
      topicTitle = "Conjunctions of Contrast",
      questionPrompt = "__________ the sudden heavy shower, the final match of the tournament continued without interruption.",
      questionSinhalaGuidance = "නිරීක්ෂණය කරන්න: හිස්තැනට පසු ඇත්තේ නාම පද ඛණ්ඩයකි (Noun Phrase - the sudden heavy shower).",
      options = listOf(
        "1. Although",
        "2. Even though",
        "3. In spite of",
        "4. Despite of"
      ),
      correctOptionIndex = 2,
      explanationSinhala = "නාම පදයක් හෝ නාම ඛණ්ඩයක් (Noun phrase) ඉදිරියෙන් පරස්පරතාව දැක්වීමට 'In spite of' හෝ 'Despite' යෙදේ. 'Although' පසුපස Subject + Verb තිබිය යුතුය. 'Despite' සමඟ 'of' නොයෙදෙන බැවින් 4 වැරදිය. එබැවින් 'In spite of' නිවැරදිය.",
      grammarRuleOrFormula = "In spite of + Noun / Noun Phrase | Although + Subject + Verb",
      examTip = "'Despite of' යනු බරපතළ ව්‍යාකරණ දෝෂයකි. 'Despite' තනිවද, 'In spite of' ලෙසද යෙදේ."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q5",
      number = 5,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.RELATIVE_CLAUSES,
      topicTitle = "Relative Pronouns (Possession)",
      questionPrompt = "We met the famous author __________ latest novel won the national literary award.",
      questionSinhalaGuidance = "කතුවරයාගේ නවකතාව (හිමිකම/Possession) දැක්වෙන Relative Pronoun එක කුමක්ද?",
      options = listOf(
        "1. who",
        "2. whose",
        "3. whom",
        "4. which"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "මෙහිදී අදහස් වන්නේ 'කතුවරයාගේ නවකතාව' (the author's novel) යන්නයි. හිමිකම (Possession) සම්බන්ධ කිරීමට යොදා ගන්නා එකම Relative Pronoun එක 'whose' වේ.",
      grammarRuleOrFormula = "whose + noun = belonging to that person/thing",
      examTip = "Who = Subject, Whom = Object, Whose = Possession (හිමිකම)."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q6",
      number = 6,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.QUESTION_TAGS,
      topicTitle = "Question Tags (Semi-Negative Adverbs)",
      questionPrompt = "Nimal rarely arrives late for the morning assembly, __________?",
      questionSinhalaGuidance = "'rarely' (කලාතුරකින්) යනු සෘණාත්මක (Negative) අරුතක් දෙන පදයකි.",
      options = listOf(
        "1. doesn't he",
        "2. does he",
        "3. did he",
        "4. isn't he"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "'rarely', 'seldom', 'hardly', 'scarcely', 'never' යන පද වාක්‍යයක ඇති විට එම වාක්‍යය සෘණ (Negative) ලෙස සලකනු ලැබේ. එබැවින් Question Tag එක ධන (Positive) විය යුතුය. 'arrives' Present Simple බැවින් 'does he?' නිවැරදි වේ.",
      grammarRuleOrFormula = "Sentence with rarely/hardly (Negative) -> Tag must be Positive (does he?)",
      examTip = "Barely/Rarely/Seldom දුටු විට tag එක සෘණ (doesn't) නොකර ධන (does) කරන්න."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q7",
      number = 7,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.PREPOSITIONS,
      topicTitle = "Dependent Prepositions",
      questionPrompt = "The principal congratulated all the winners __________ their outstanding achievements in the sports meet.",
      questionSinhalaGuidance = "'Congratulate' ක්‍රියා පදය සමඟ භාවිත වන නිවැරදි නිපාතය (Preposition).",
      options = listOf(
        "1. for",
        "2. on",
        "3. at",
        "4. with"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "ඉංග්‍රීසි ව්‍යාකරණ නීතිය අනුව 'congratulate someone ON something' යෙදේ. සිංහලෙන් 'සඳහා' යැයි සිතා 'for' යෙදීම බහුල වැරැද්දකි. නිවැරදි යෙදුම 'congratulate ... on' වේ.",
      grammarRuleOrFormula = "Congratulate (someone) + ON + (achievement/success)",
      examTip = "Congratulate on / Insist on / Rely on / Depend on යනු නිතර විභාගයේ අසන prepositions වේ."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q8",
      number = 8,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.ERROR_CORRECTION,
      topicTitle = "Subject-Verb Agreement",
      questionPrompt = "Neither the headmaster nor the assistant teachers __________ present at the meeting yesterday.",
      questionSinhalaGuidance = "'Neither ... nor' යෙදුමේදී ක්‍රියා පදයට සමීපතම කර්තෘ (Subject) අනුව ක්‍රියා පදය තීරණය වේ.",
      options = listOf(
        "1. was",
        "2. were",
        "3. is",
        "4. are"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "'Neither A nor B' හෝ 'Either A or B' ව්‍යුහයේදී ක්‍රියා පදය තීරණය වන්නේ ක්‍රියා පදයට ළඟින්ම සිටින නාම පදය (the assistant teachers - බහුවචන) අනුවයි. 'yesterday' ඇති බැවින් අතීත කාල බහුවචන 'were' නිවැරදි වේ.",
      grammarRuleOrFormula = "Neither A nor B + Verb (Verb agrees with the closer subject B)",
      examTip = "ළඟම සිටින කර්තෘ 'assistant teachers' (Plural) බැවින් 'were' යෙදේ."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q9",
      number = 9,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.VOCABULARY,
      topicTitle = "Collocations and Formal Phrasing",
      questionPrompt = "Before submitting your application, make sure you have fulfilled all the necessary __________.",
      questionSinhalaGuidance = "'Fulfilled all the necessary ...' සමඟ ගැලපෙන නිවැරදි පදය.",
      options = listOf(
        "1. requirements",
        "2. requires",
        "3. requirings",
        "4. require"
      ),
      correctOptionIndex = 0,
      explanationSinhala = "'necessary' යනු Adjective එකකි. Adjective එකකට පසුව නාම පදයක් (Noun) පැමිණිය යුතුය. 'all the' ඇති බැවින් බහුවචන නාම පදයක් වන 'requirements' (අවශ්‍යතා) යෙදිය යුතුය.",
      grammarRuleOrFormula = "Adjective (necessary) + Plural Noun (requirements)",
      examTip = "Suffixes: -ment, -tion, -ity, -ness සාමාන්‍යයෙන් Nouns සාදයි."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q10",
      number = 10,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.TENSES,
      topicTitle = "Past Perfect vs Simple Past",
      questionPrompt = "By the time the fire brigade reached the factory, the workers __________ the main fire.",
      questionSinhalaGuidance = "අතීතයේ සිදුවීම් දෙකකින් පළමුව සිදුවූ ක්‍රියාව දැක්වීම (By the time ...).",
      options = listOf(
        "1. already put out",
        "2. had already put out",
        "3. have already put out",
        "4. are putting out"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "අතීතයේ සිදුවීම් දෙකක් සිදුවූ විට, පළමුව සිදුවී අවසන් වූ ක්‍රියාව Past Perfect (had + V3 -> had put out) මගින්ද, දෙවනුව සිදුවූ ක්‍රියාව Simple Past (reached) මගින්ද දක්වයි.",
      grammarRuleOrFormula = "1st Past Action = Past Perfect (had + V3) | 2nd Past Action = Simple Past (V2)",
      examTip = "'By the time + Past Simple' පැමිණි විට අනෙක් කොටසට 'had + V3' යොදන්න."
    ),

    // -------------------------------------------------------------------------
    // GRADE 10 QUESTIONS (Intermediate Grammar, Transformations & Clauses)
    // -------------------------------------------------------------------------
    EnglishShortNoteQuestionItem(
      id = "eng_q11",
      number = 11,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.TENSES,
      topicTitle = "Present Perfect with Since and For",
      questionPrompt = "Mr. Perera __________ English at this school since 2015.",
      questionSinhalaGuidance = "'since 2015' (2015 සිට අඛණ්ඩව) දැක්වෙන කාල ප්‍රකාශනය.",
      options = listOf(
        "1. teaches",
        "2. is teaching",
        "3. has been teaching",
        "4. taught"
      ),
      correctOptionIndex = 2,
      explanationSinhala = "අතීතයේ ආරම්භ වී වර්තමානය දක්වා අඛණ්ඩව සිදුවන ක්‍රියාවන් සඳහා 'since / for' සමඟ Present Perfect Continuous (has/have been + V-ing) යෙදේ. Mr. Perera ඒකවචන බැවින් 'has been teaching' නිවැරදිය.",
      grammarRuleOrFormula = "Subject (singular) + has been + Verb-ing + since/for + time",
      examTip = "Since = නිශ්චිත ආරම්භක අවස්ථාව (since 2015), For = කාල පරිච්ඡේදය (for 10 years)."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q12",
      number = 12,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.CONDITIONALS,
      topicTitle = "Conditional Sentences (Type 2)",
      questionPrompt = "If I __________ enough money, I would travel around the world.",
      questionSinhalaGuidance = "මනඃකල්පිත වර්තමාන/අනාගත අවස්ථාවක් (Second Conditional - would travel).",
      options = listOf(
        "1. have",
        "2. had",
        "3. will have",
        "4. would have"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "Main clause එකෙහි 'would + V1' (would travel) ඇති විට If clause එක Simple Past (V2 -> had) විය යුතුය. මෙය Type 2 Conditional වේ.",
      grammarRuleOrFormula = "If + Past Simple (V2) , Subject + would + Base Verb (V1)",
      examTip = "If I had money = මා සතුව මුදල් තිබුණා නම් (නමුත් සැබවින්ම නැත)."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q13",
      number = 13,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.PASSIVE_VOICE,
      topicTitle = "Passive Voice (Simple Past)",
      questionPrompt = "Active: Alexander Graham Bell invented the telephone in 1876.\nPassive: The telephone __________ by Alexander Graham Bell in 1876.",
      questionSinhalaGuidance = "Simple Past වාක්‍යයක් Passive Voice බවට පෙරළීම.",
      options = listOf(
        "1. is invented",
        "2. was invented",
        "3. had invented",
        "4. were invented"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "Simple Past Active ක්‍රියාවක් Passive කිරීමේදී 'was/were + Past Participle (V3)' යෙදේ. 'The telephone' ඒකවචන බැවින් 'was invented' නිවැරදි වේ.",
      grammarRuleOrFormula = "Past Simple Passive = was / were + Past Participle (V3)",
      examTip = "The telephone ඒකවචන බැවින් was ද, බහුවචන නම් were ද යෙදිය යුතුය."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q14",
      number = 14,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.PREPOSITIONS,
      topicTitle = "Prepositions of Time and Place",
      questionPrompt = "Our family usually goes to Nuwara Eliya __________ April __________ train.",
      questionSinhalaGuidance = "මාස (Months) සහ ප්‍රවාහන මාධ්‍ය (Means of Transport) සඳහා යෙදෙන prepositions.",
      options = listOf(
        "1. on / with",
        "2. in / by",
        "3. at / by",
        "4. in / on"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "මාස (Months) සහ අවුරුදු (Years) සඳහා 'in' යෙදේ (in April). ප්‍රවාහන මාධ්‍ය සඳහා 'by' යෙදේ (by train, by bus, by plane).",
      grammarRuleOrFormula = "in + month/year | by + vehicle (train, bus, plane, car)",
      examTip = "In April, In 2026, On Monday, On 5th May, At 7:00 AM."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q15",
      number = 15,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.CONJUNCTIONS,
      topicTitle = "Conjunctions: 'Unless' vs 'If not'",
      questionPrompt = "You will not pass the examination __________ you study consistently every day.",
      questionSinhalaGuidance = "'නොකළහොත්' (Unless = If you do not) අරුත දෙන සම්බන්ධක පදය.",
      options = listOf(
        "1. if",
        "2. unless",
        "3. although",
        "4. because"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "'Unless' යන්නෙහි තේරුම 'If ... not' (එසේ නොකළහොත්) යන්නයි. 'ඔබ දිනපතා පාඩම් නොකළහොත් ඔබට විභාගය සමත් විය නොහැක' යන අර්ථය දීමට 'unless' යෙදිය යුතුය.",
      grammarRuleOrFormula = "Unless = If + not (Unless you study = If you do not study)",
      examTip = "'Unless' සමඟ වාක්‍යය තුළ තවත් 'not' නොයොදන්න (Unless you don't study යනු වැරදියි)."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q16",
      number = 16,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.QUESTION_TAGS,
      topicTitle = "Question Tags (Imperatives & Suggestions)",
      questionPrompt = "Let's organize a beach clean-up campaign this weekend, __________?",
      questionSinhalaGuidance = "'Let's' (යෝජනාවක් / Suggestion) සඳහා යෙදෙන විශේෂ Question Tag එක කුමක්ද?",
      options = listOf(
        "1. don't we",
        "2. shall we",
        "3. will you",
        "4. aren't we"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "'Let's' (Let us) මගින් ආරම්භ වන යෝජනාවලදී සම්මත Question Tag එක වන්නේ 'shall we?' වේ.",
      grammarRuleOrFormula = "Let's + Verb ... , shall we?",
      examTip = "විධානයක් (Open the door) නම් 'will you?', යෝජනාවක් (Let's go) නම් 'shall we?' වේ."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q17",
      number = 17,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.RELATIVE_CLAUSES,
      topicTitle = "Defining Relative Clauses (Things vs People)",
      questionPrompt = "The solar panels __________ were installed on our school roof generate sufficient electricity for all classrooms.",
      questionSinhalaGuidance = "'solar panels' (අජීවී භාණ්ඩ/Things) සඳහා යෙදෙන Relative Pronoun එක කුමක්ද?",
      options = listOf(
        "1. who",
        "2. which",
        "3. whom",
        "4. whose"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "අජීවී ද්‍රව්‍ය හෝ සතුන් (Things or Animals) සම්බන්ධ කිරීමට 'which' හෝ 'that' භාවිත වේ. 'who' සහ 'whom' මිනිසුන් සඳහා පමණක් යෙදේ.",
      grammarRuleOrFormula = "Which / That = for objects, things, and animals",
      examTip = "සූර්ය පැනල (solar panels) යනු අජීවී ද්‍රව්‍ය බැවින් 'which' නිවැරදිය."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q18",
      number = 18,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.ERROR_CORRECTION,
      topicTitle = "Countable vs Uncountable Nouns",
      questionPrompt = "The teacher gave us __________ valuable advice on how to prepare for the final term test.",
      questionSinhalaGuidance = "'Advice' (උපදෙස්) යනු Uncountable නාම පදයකි. එය ඉදිරියෙන් 'an' නොයෙදේ.",
      options = listOf(
        "1. an",
        "2. a piece of",
        "3. many",
        "4. few"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "'Advice', 'Information', 'Furniture', 'News' යනු Uncountable Nouns වේ. ඒවා සමඟ 'an advice' හෝ 'many advices' යෙදිය නොහැක. තනි උපදේශයක් දැක්වීමට 'a piece of advice' හෝ සමස්තයක් ලෙස 'some advice' යෙදේ.",
      grammarRuleOrFormula = "Uncountable noun (Advice) -> a piece of advice / some advice (Never 'an advice')",
      examTip = "Information, Advice, Homework, Luggage බහුවචන (-s) කළ නොහැක."
    ),

    // -------------------------------------------------------------------------
    // GRADE 10 FOUNDATIONAL QUESTIONS (Grammar, Modals, Forms & Usage)
    // -------------------------------------------------------------------------
    EnglishShortNoteQuestionItem(
      id = "eng_q19",
      number = 19,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.TENSES,
      topicTitle = "Simple Past vs Simple Present",
      questionPrompt = "Yesterday, my brother __________ a new bicycle from the shop in town.",
      questionSinhalaGuidance = "'Yesterday' (ඊයේ) යනු අතීත කාල සලකුණකි (Simple Past).",
      options = listOf(
        "1. buys",
        "2. bought",
        "3. is buying",
        "4. has bought"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "'Yesterday', 'last week', 'in 2020', 'ago' යන කාල වාචී පද ඇති විට ක්‍රියා පදය අනිවාර්යයෙන්ම Simple Past (V2) විය යුතුය. 'Buy' හි Past tense එක 'bought' වේ.",
      grammarRuleOrFormula = "Past time marker (yesterday) + Past Simple Verb (bought)",
      examTip = "Buy -> Bought, Catch -> Caught, Bring -> Brought, Think -> Thought."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q20",
      number = 20,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.QUESTION_TAGS,
      topicTitle = "Basic Question Tags (Simple Present with 'be')",
      questionPrompt = "They are excited about the annual school trip, __________?",
      questionSinhalaGuidance = "වාක්‍යයේ 'are' (ධන) ඇති බැවින් Tag එක සෘණ (aren't) විය යුතුය.",
      options = listOf(
        "1. are they",
        "2. aren't they",
        "3. do they",
        "4. don't they"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "ප්‍රධාන වාක්‍යය ධනාත්මක (Positive) වන අතර සහායක ක්‍රියා පදය 'are' වේ. එබැවින් Question Tag එක 'aren't they?' විය යුතුය.",
      grammarRuleOrFormula = "They are ... -> aren't they?",
      examTip = "වාක්‍යයේ ඇති සහායක ක්‍රියාව (is, are, was, were) කෙලින්ම tag එකට ගෙන සෘණ කරන්න."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q21",
      number = 21,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.PREPOSITIONS,
      topicTitle = "Prepositions of Place (in / on / at / under)",
      questionPrompt = "The cat is sleeping comfortably __________ the warm blanket on the sofa.",
      questionSinhalaGuidance = "බ්ලැන්කට්ටුවට 'යටින්' නිදාගෙන සිටින බව දැක්වෙන නිපාතය.",
      options = listOf(
        "1. under",
        "2. above",
        "3. over",
        "4. between"
      ),
      correctOptionIndex = 0,
      explanationSinhala = "යමකට යටින් පිහිටීම දැක්වීමට 'under' (යට) යෙදේ.",
      grammarRuleOrFormula = "Under = below the surface of something or covered by it",
      examTip = "Under the tree, under the bed, under the blanket."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q22",
      number = 22,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.VOCABULARY,
      topicTitle = "Opposites and Antonyms with Prefixes",
      questionPrompt = "The opposite of 'responsible' formed by adding a prefix is __________.",
      questionSinhalaGuidance = "'responsible' (වගකීම් සහගත) පදයට උපසර්ගයක් (Prefix) එක්කර ප්‍රතිවිරුද්ධ පදය සෑදීම.",
      options = listOf(
        "1. unresponsible",
        "2. irresponsible",
        "3. disresponsible",
        "4. inresponsible"
      ),
      correctOptionIndex = 1,
      explanationSinhala = "'r' අකුරෙන් පටන් ගන්නා බොහෝ වචනවල විරුද්ධ පදය සෑදෙන්නේ 'ir-' උපසර්ගය (Prefix) මගිනි. Responsible -> Irresponsible, Regular -> Irregular, Relevant -> Irrelevant.",
      grammarRuleOrFormula = "Prefix 'ir-' + word starting with 'r' (ir + responsible = irresponsible)",
      examTip = "legal -> illegal (il-), polite -> impolite (im-), regular -> irregular (ir-)."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q23",
      number = 23,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.CONJUNCTIONS,
      topicTitle = "Coordinating Conjunctions (and, but, so, or)",
      questionPrompt = "Kavindu was feeling unwell, __________ he still came to school to submit the science assignment.",
      questionSinhalaGuidance = "අසනීපයෙන් සිටියත්, එහෙත් (but / yet) ඔහු පැමිණියේය යන පරස්පර අර්ථය.",
      options = listOf(
        "1. because",
        "2. so",
        "3. but",
        "4. or"
      ),
      correctOptionIndex = 2,
      explanationSinhala = "පෙර වාක්‍යාංශය හා පසු වාක්‍යාංශය අතර පරස්පරයක් (Contrast) ඇති බැවින් 'but' (නමුත්/එහෙත්) නිවැරදි සංයෝජකය වේ.",
      grammarRuleOrFormula = "Contrast Connector = but / however",
      examTip = "Because = හේතුව, So = ප්‍රතිඵලය, But = පරස්පරය, Or = විකල්පය."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q24",
      number = 24,
      gradeLevel = EnglishGradeLevel.GRADE_10,
      category = EnglishSkillCategory.ERROR_CORRECTION,
      topicTitle = "Plural Irregular Nouns",
      questionPrompt = "The dentist examined the patient's __________ very carefully.",
      questionSinhalaGuidance = "'Tooth' (දත) හි නිවැරදි බහුවචනය (Irregular Plural).",
      options = listOf(
        "1. tooths",
        "2. teeths",
        "3. teeth",
        "4. toothes"
      ),
      correctOptionIndex = 2,
      explanationSinhala = "'Tooth' හි අක්‍රමවත් බහුවචන රූපය 'teeth' වේ. එයට නැවත 's' එක් නොවේ.",
      grammarRuleOrFormula = "Singular: Tooth -> Plural: Teeth (Foot -> Feet, Child -> Children, Man -> Men)",
      examTip = "Teeths හෝ Tooths යනු වැරදි වචන වේ. නිවැරදි පදය Teeth වේ."
    ),
    EnglishShortNoteQuestionItem(
      id = "eng_q25",
      number = 25,
      gradeLevel = EnglishGradeLevel.GRADE_11,
      category = EnglishSkillCategory.CONDITIONALS,
      topicTitle = "Conditional Sentences (Type 1)",
      questionPrompt = "If we __________ the early morning train, we will arrive in Colombo before noon.",
      questionSinhalaGuidance = "Main clause එකෙහි 'will arrive' (First Conditional) ඇති විට If කොටස Simple Present විය යුතුය.",
      options = listOf(
        "1. catch",
        "2. caught",
        "3. had caught",
        "4. will catch"
      ),
      correctOptionIndex = 0,
      explanationSinhala = "First Conditional හිදී If clause එක Simple Present (catch) වන අතර Main clause එක 'will + V1' (will arrive) වේ.",
      grammarRuleOrFormula = "If + Simple Present (V1) , Subject + will + V1",
      examTip = "If we catch the train -> we will arrive."
    )
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnglishShortNotesAutoCheckerScreen(
  onBack: () -> Unit,
  onOpenPdfDriveViewer: (pdfUrl: String, title: String) -> Unit
) {
  val context = LocalContext.current

  var selectedGradeFilter by remember { mutableStateOf(EnglishGradeLevel.ALL) }
  var selectedSkillCategory by remember { mutableStateOf(EnglishSkillCategory.ALL) }
  var searchQuery by remember { mutableStateOf("") }
  var currentTabMode by remember { mutableIntStateOf(0) } // 0: Questions, 1: Grammar Rules, 2: Timed Quiz, 3: Bookmarks

  val userAnswers = remember { mutableStateMapOf<String, Int>() }
  val bookmarkedQuestionIds = remember { mutableStateListOf<String>() }

  var ttsInstance by remember { mutableStateOf<TextToSpeech?>(null) }

  DisposableEffect(Unit) {
    var tts: TextToSpeech? = null
    tts = TextToSpeech(context) { status ->
      if (status == TextToSpeech.SUCCESS) {
        tts?.language = Locale.ENGLISH
      }
    }
    ttsInstance = tts
    onDispose {
      tts?.stop()
      tts?.shutdown()
    }
  }

  fun speakEnglish(text: String) {
    ttsInstance?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "eng_tts_${System.currentTimeMillis()}")
  }

  // Filtered questions
  val filteredQuestions = remember(selectedGradeFilter, selectedSkillCategory, searchQuery, currentTabMode, bookmarkedQuestionIds.size) {
    EnglishShortNotesRepository.allQuestions.filter { q ->
      val matchesGrade = (selectedGradeFilter == EnglishGradeLevel.ALL) || (q.gradeLevel == selectedGradeFilter)
      val matchesCategory = (selectedSkillCategory == EnglishSkillCategory.ALL) || (q.category == selectedSkillCategory)
      val matchesSearch = searchQuery.isBlank() ||
        q.questionPrompt.contains(searchQuery, ignoreCase = true) ||
        q.topicTitle.contains(searchQuery, ignoreCase = true) ||
        q.questionSinhalaGuidance.contains(searchQuery, ignoreCase = true)
      val matchesBookmark = if (currentTabMode == 3) bookmarkedQuestionIds.contains(q.id) else true

      matchesGrade && matchesCategory && matchesSearch && matchesBookmark
    }
  }

  val totalAnswered = userAnswers.size
  val correctCount = userAnswers.count { (qId, selectedIdx) ->
    val question = EnglishShortNotesRepository.allQuestions.firstOrNull { it.id == qId }
    question?.correctOptionIndex == selectedIdx
  }
  val accuracyPercent = if (totalAnswered > 0) (correctCount * 100 / totalAnswered) else 0

  Scaffold(
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    topBar = {
      Column(modifier = Modifier.fillMaxWidth()) {
        // Ultra-slim black/navy top bar (Height 42dp - minimal vertical space)
        Surface(
          color = Color(0xFF0F172A),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(42.dp)
              .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = onBack,
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(19.dp)
              )
            }
            Spacer(modifier = Modifier.width(2.dp))
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "09/10/11 ඉංග්‍රීසි කෙටි සටහන් & Auto-Checker",
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Spacer(modifier = Modifier.width(5.dp))
              Surface(
                shape = RoundedCornerShape(3.dp),
                color = Color(0xFF10B981)
              ) {
                Text(
                  text = "100%",
                  fontSize = 7.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
            }

            IconButton(
              onClick = {
                userAnswers.clear()
                Toast.makeText(context, "සියලු පිළිතුරු Reset කරන ලදී", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = Color(0xFF38BDF8),
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }

        // Clean light Tab Bar on white background (not dark/black)
        Surface(
          color = Color.White,
          shadowElevation = 1.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          ScrollableTabRow(
            selectedTabIndex = currentTabMode,
            containerColor = Color.White,
            contentColor = Color(0xFF0F172A),
            edgePadding = 8.dp,
            indicator = { tabPositions ->
              if (currentTabMode < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                  modifier = Modifier.tabIndicatorOffset(tabPositions[currentTabMode]),
                  height = 2.5.dp,
                  color = Color(0xFF0284C7)
                )
              }
            },
            divider = {
              HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 0.8.dp)
            }
          ) {
            val tabTitles = listOf(
              "🎯 ප්‍රශ්නාවලිය (${filteredQuestions.size})",
              "📖 ව්‍යාකරණ කෙටි සටහන්",
              "⚡ විභාග අනුකරණය",
              "⭐ සුරැකි (${bookmarkedQuestionIds.size})"
            )
            tabTitles.forEachIndexed { index, title ->
              val isSelected = currentTabMode == index
              Tab(
                selected = isSelected,
                onClick = { currentTabMode = index },
                text = {
                  Text(
                    text = title,
                    fontSize = 11.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color(0xFF0F172A) else Color(0xFF64748B)
                  )
                },
                modifier = Modifier.height(38.dp)
              )
            }
          }
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF8FAFC))
    ) {

      if (currentTabMode == 0 || currentTabMode == 3) {
        // Stats Banner
        Surface(
          color = Color.White,
          shadowElevation = 1.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "ඔබගේ ප්‍රගතිය (Score Progress)",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF64748B)
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "$correctCount / $totalAnswered නිවැරදියි",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (accuracyPercent >= 75) Color(0xFF059669) else if (accuracyPercent >= 50) Color(0xFFD97706) else Color(0xFFDC2626)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFFE2E8F0)
                ) {
                  Text(
                    text = "$accuracyPercent% Accuracy",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }

            LinearProgressIndicator(
              progress = { if (EnglishShortNotesRepository.allQuestions.isNotEmpty()) totalAnswered.toFloat() / EnglishShortNotesRepository.allQuestions.size else 0f },
              modifier = Modifier
                .width(100.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = Color(0xFF10B981),
              trackColor = Color(0xFFE2E8F0)
            )
          }
        }

        // Grade Filter Chips
        LazyRow(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(EnglishGradeLevel.values()) { grade ->
            val isSelected = selectedGradeFilter == grade
            FilterChip(
              selected = isSelected,
              onClick = { selectedGradeFilter = grade },
              label = {
                Text(
                  text = grade.label,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = grade.badgeColor,
                selectedLabelColor = Color.White
              )
            )
          }
        }

        // Skill Category Filter Chips
        LazyRow(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(EnglishSkillCategory.values()) { cat ->
            val isSelected = selectedSkillCategory == cat
            Surface(
              onClick = { selectedSkillCategory = cat },
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) cat.primaryColor else Color.White,
              border = BorderStroke(1.dp, if (isSelected) cat.primaryColor else Color(0xFFCBD5E1)),
              modifier = Modifier.padding(vertical = 2.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(cat.iconEmoji, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = cat.displayName,
                  fontSize = 10.5.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else Color(0xFF334155)
                )
              }
            }
          }
        }

        // Search Bar
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("ප්‍රශ්න හෝ ව්‍යාකරණ මාතෘකා සොයන්න...", fontSize = 12.sp) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(18.dp)) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .height(50.dp)
        )

        // Questions List
        if (filteredQuestions.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(24.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("🔍", fontSize = 40.sp)
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = if (currentTabMode == 3) "සුරැකි ප්‍රශ්න කිසිවක් නැත." else "මෙම නිර්ණායකවලට ගැලපෙන ප්‍රශ්න හමු නොවීය.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
              )
              Text(
                text = "වෙනත් ශ්‍රේණියක් හෝ මාතෘකාවක් තෝරා බලන්න.",
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
              )
            }
          }
        } else {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 32.dp)
          ) {
            itemsIndexed(filteredQuestions, key = { _, item -> item.id }) { index, qItem ->
              val selectedOption = userAnswers[qItem.id]
              val isBookmarked = bookmarkedQuestionIds.contains(qItem.id)

              EnglishQuestionCard(
                item = qItem,
                selectedIndex = selectedOption,
                isBookmarked = isBookmarked,
                onSelectOption = { optIdx ->
                  userAnswers[qItem.id] = optIdx
                },
                onToggleBookmark = {
                  if (isBookmarked) {
                    bookmarkedQuestionIds.remove(qItem.id)
                  } else {
                    bookmarkedQuestionIds.add(qItem.id)
                  }
                },
                onSpeak = { text -> speakEnglish(text) }
              )
            }
          }
        }
      } else if (currentTabMode == 1) {
        // GRAMMAR RULES AND SHORT NOTES SUMMARY
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp)
        ) {
          item {
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("📚", fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "O/L & Middle School English Short Notes Rules",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Text(
                    text = "09, 10 සහ 11 ශ්‍රේණි සඳහා විභාගයට අත්‍යවශ්‍ය වන සියලුම මූලික ව්‍යාකරණ සූත්‍ර හා කෙටි සටහන් එකතුව.",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                  )
                }
              }
            }
          }

          items(EnglishShortNotesRepository.grammarRulesList) { rule ->
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(rule.category.iconEmoji, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = rule.title,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A)
                    )
                  }
                  IconButton(
                    onClick = { speakEnglish(rule.examples.firstOrNull() ?: rule.title) },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.VolumeUp,
                      contentDescription = "Pronounce",
                      tint = Color(0xFF0284C7),
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Formula Banner
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color(0xFF0F172A),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text("⚡ Formula:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = rule.formula,
                      fontSize = 11.sp,
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFFF1F5F9)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = rule.explanationSinhala,
                  fontSize = 12.sp,
                  color = Color(0xFF334155),
                  lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = "නිදසුන් (Examples):",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF059669)
                )

                rule.examples.forEach { eg ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text("•", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = eg,
                      fontSize = 11.5.sp,
                      fontFamily = FontFamily.SansSerif,
                      color = Color(0xFF1E293B)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFFFEF2F2),
                  border = BorderStroke(1.dp, Color(0xFFFECACA)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("⚠️", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = rule.commonMistake,
                      fontSize = 10.5.sp,
                      color = Color(0xFF991B1B)
                    )
                  }
                }
              }
            }
          }
        }
      } else if (currentTabMode == 2) {
        // TIMED EXAM SIMULATOR
        TimedEnglishExamHall(
          questions = EnglishShortNotesRepository.allQuestions.take(15),
          onFinish = { score, total ->
            Toast.makeText(context, "විභාගය අවසන් විය! ලකුණු: $score / $total", Toast.LENGTH_LONG).show()
          },
          onSpeak = { text -> speakEnglish(text) }
        )
      }
    }
  }
}

@Composable
fun EnglishQuestionCard(
  item: EnglishShortNoteQuestionItem,
  selectedIndex: Int?,
  isBookmarked: Boolean,
  onSelectOption: (Int) -> Unit,
  onToggleBookmark: () -> Unit,
  onSpeak: (String) -> Unit
) {
  val isAnswered = selectedIndex != null
  val isCorrect = selectedIndex == item.correctOptionIndex

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(
      width = if (isAnswered) 1.5.dp else 1.dp,
      color = if (!isAnswered) Color(0xFFE2E8F0) else if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444)
    ),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("eng_question_card_${item.number}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Number + Grade Badge + Category + Bookmark + Speaker
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = item.category.primaryColor
          ) {
            Text(
              text = "Q${item.number}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Surface(
            shape = RoundedCornerShape(4.dp),
            color = item.gradeLevel.badgeColor.copy(alpha = 0.15f)
          ) {
            Text(
              text = item.gradeLevel.label,
              fontSize = 9.5.sp,
              fontWeight = FontWeight.Bold,
              color = item.gradeLevel.badgeColor,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { onSpeak(item.questionPrompt) },
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "Read Aloud",
              tint = Color(0xFF0284C7),
              modifier = Modifier.size(18.dp)
            )
          }

          IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = if (isBookmarked) Icons.Default.Star else Icons.Default.StarBorder,
              contentDescription = "Bookmark",
              tint = if (isBookmarked) Color(0xFFF59E0B) else Color(0xFF94A3B8),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = item.topicTitle,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = item.category.primaryColor
      )

      Spacer(modifier = Modifier.height(6.dp))

      // English Question Prompt
      Text(
        text = item.questionPrompt,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A),
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Sinhala Guidance
      Text(
        text = "💡 ${item.questionSinhalaGuidance}",
        fontSize = 11.sp,
        color = Color(0xFF475569)
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Multiple Choice Options
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item.options.forEachIndexed { optIdx, optText ->
          val isThisSelected = selectedIndex == optIdx
          val isThisCorrect = optIdx == item.correctOptionIndex

          val optionBg = when {
            !isAnswered -> Color(0xFFF8FAFC)
            isThisCorrect -> Color(0xFFECFDF5)
            isThisSelected && !isCorrect -> Color(0xFFFEF2F2)
            else -> Color(0xFFF8FAFC)
          }

          val optionBorder = when {
            !isAnswered -> if (isThisSelected) Color(0xFF0284C7) else Color(0xFFCBD5E1)
            isThisCorrect -> Color(0xFF10B981)
            isThisSelected && !isCorrect -> Color(0xFFEF4444)
            else -> Color(0xFFE2E8F0)
          }

          Surface(
            onClick = { onSelectOption(optIdx) },
            shape = RoundedCornerShape(10.dp),
            color = optionBg,
            border = BorderStroke(1.2.dp, optionBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = optText,
                fontSize = 12.5.sp,
                fontWeight = if (isThisSelected || (isAnswered && isThisCorrect)) FontWeight.Bold else FontWeight.Normal,
                color = when {
                  !isAnswered -> Color(0xFF1E293B)
                  isThisCorrect -> Color(0xFF065F46)
                  isThisSelected && !isCorrect -> Color(0xFF991B1B)
                  else -> Color(0xFF64748B)
                },
                modifier = Modifier.weight(1f)
              )

              if (isAnswered) {
                if (isThisCorrect) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Correct",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(18.dp)
                  )
                } else if (isThisSelected) {
                  Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Incorrect",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Detailed Explanation Section (100% Comprehensive Auto-Check)
      AnimatedVisibility(
        visible = isAnswered,
        enter = fadeIn() + expandVertically()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isCorrect) Color(0xFFF0FDF4) else Color(0xFFFFFBEB),
            border = BorderStroke(1.dp, if (isCorrect) Color(0xFF86EFAC) else Color(0xFFFDE68A)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (isCorrect) "🎉 නිවැරදියි! (100% Correct)" else "❌ වැරදියි! නිවැරදි විවරණය පහතින් බලන්න:",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = if (isCorrect) Color(0xFF15803D) else Color(0xFFB45309)
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              // Sinhala Explanation
              Text(
                text = item.explanationSinhala,
                fontSize = 11.5.sp,
                color = Color(0xFF1E293B),
                lineHeight = 17.sp
              )

              Spacer(modifier = Modifier.height(6.dp))

              // Grammar Rule / Formula
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF0F172A),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("📐 නීතිය:", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = item.grammarRuleOrFormula,
                    fontSize = 10.5.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFF8FAFC)
                  )
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              // Exam Tip
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎯 විභාග ඉඟිය:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = item.examTip,
                  fontSize = 10.sp,
                  color = Color(0xFF475569)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun TimedEnglishExamHall(
  questions: List<EnglishShortNoteQuestionItem>,
  onFinish: (score: Int, total: Int) -> Unit,
  onSpeak: (String) -> Unit
) {
  var currentIndex by remember { mutableIntStateOf(0) }
  val answers = remember { mutableStateMapOf<Int, Int>() }
  var isSubmitted by remember { mutableStateOf(false) }

  val totalQuestions = questions.size
  val currentQuestion = questions.getOrNull(currentIndex)

  val score = answers.count { (qIdx, selectedOpt) ->
    questions.getOrNull(qIdx)?.correctOptionIndex == selectedOpt
  }

  if (isSubmitted) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("🏆", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "විභාග ප්‍රතිඵල වාර්තාව (Exam Result)",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "ලකුණු: $score / $totalQuestions (${if (totalQuestions > 0) score * 100 / totalQuestions else 0}%)",
          fontSize = 20.sp,
          fontWeight = FontWeight.ExtraBold,
          color = if (score >= totalQuestions * 0.75) Color(0xFF10B981) else Color(0xFF0284C7)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            answers.clear()
            currentIndex = 0
            isSubmitted = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("නැවත විභාගය කරන්න (Retake Exam)")
        }
      }
    }
  } else if (currentQuestion != null) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      // Top Progress
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ප්‍රශ්න ${currentIndex + 1} / $totalQuestions",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = Color(0xFF0F172A)
        )

        LinearProgressIndicator(
          progress = { (currentIndex + 1).toFloat() / totalQuestions },
          modifier = Modifier
            .width(120.dp)
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = Color(0xFF0284C7)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = currentQuestion.gradeLevel.badgeColor
            ) {
              Text(
                text = currentQuestion.gradeLevel.label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            IconButton(
              onClick = { onSpeak(currentQuestion.questionPrompt) },
              modifier = Modifier.size(24.dp)
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = "Audio", tint = Color(0xFF0284C7))
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = currentQuestion.questionPrompt,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "💡 ${currentQuestion.questionSinhalaGuidance}",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )

          Spacer(modifier = Modifier.height(12.dp))

          currentQuestion.options.forEachIndexed { optIdx, optText ->
            val isSelected = answers[currentIndex] == optIdx
            Surface(
              onClick = { answers[currentIndex] = optIdx },
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) Color(0xFFE0F2FE) else Color(0xFFF8FAFC),
              border = BorderStroke(1.dp, if (isSelected) Color(0xFF0284C7) else Color(0xFFCBD5E1)),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Text(
                text = optText,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF0369A1) else Color(0xFF1E293B),
                modifier = Modifier.padding(12.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Button(
          onClick = { if (currentIndex > 0) currentIndex-- },
          enabled = currentIndex > 0,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64748B)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("පෙර ප්‍රශ්නය")
        }

        if (currentIndex < totalQuestions - 1) {
          Button(
            onClick = { currentIndex++ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("මීළඟ ප්‍රශ්නය")
          }
        } else {
          Button(
            onClick = {
              isSubmitted = true
              onFinish(score, totalQuestions)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("විභාගය අවසන් කරන්න")
          }
        }
      }
    }
  }
}
