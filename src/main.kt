import java.io.File
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/** The chat log file that we will loop through **/
val inputFile: File = File("C:\\Users\\lewis\\IdeaProjects\\SkypeCallAnalyzer\\src\\file.txt")

/** Scanner to loop through the file **/
val scanner = Scanner(inputFile)

/** We use this string to determine if a line from the Skype output log was a call or not. Only calls > 0's
 ** will contain this string. Failed calls will be omitted from the results **/
val stringToCheck = "Call ended, duration"

/** The total number of valid (> 1 second) calls found in the log file **/
var totalCalls = 0

/** The total (sum) of all call times found in the log file **/
var totalTime = 0.0

/** The total number of call time hours **/
var totalHours = 0.0

/** The total number of call time minutes **/
var totalMinutes = 0.0

/** The total number of call time seconds **/
var totalSeconds = 0.0

/** The output format for our call time **/
val outputFormat = SimpleDateFormat("HH:mm:ss")

fun main(args: Array<String>)
{
    /** Loop through the chat log until we reach the end **/
    while (scanner.hasNextLine())
    {
        readCurrentLine()
    }
    scanner.close()

    /** Display the results **/
    displayResults()
}

fun readCurrentLine()
{
    /** Return the current line of the chat log **/
    var currentLine: String = scanner.nextLine()

    /** If the current line contains our string then it must be a call **/
    if (currentLine.contains(stringToCheck))
    {
        /** We found a call so update the total calls counter **/
        totalCalls++

        /** Trim the current line to contain only the call duration **/
        currentLine = currentLine.substring(47, currentLine.lastIndexOf(" "))

        /** Get the parsed Skype call time **/
        val parsedTime = parseSkypeCallTime(currentLine)

        /** Store the sum of all call times **/
        totalTime = sumSkypeCallTime(parsedTime)
    }
}

fun parseSkypeCallTime(currentLine: String) :String
{
    try
    {
        /** Try to parse a call that is > 1 hour long **/
        val inputFormat = SimpleDateFormat("HH:mm:ss")
        return outputFormat.format(inputFormat.parse(currentLine))
    }
    catch (e: ParseException)
    {
        /** If that fails then the call must be < 1 hour long **/
        val inputFormat = SimpleDateFormat("mm:ss")
        return outputFormat.format(inputFormat.parse(currentLine))
    }
}

fun sumSkypeCallTime(parsedTime: String): Double
{
    /** Split the call time by semicolon **/
    val splitTime : List<String> = parsedTime.split(":")

    /** parse the hours, minutes and seconds respectively from our split list **/
    totalHours += splitTime[0].toDouble()
    totalMinutes += splitTime[1].toDouble()
    totalSeconds += splitTime[2].toDouble()

    /** Return our total call time in hours **/
    return totalHours + (totalMinutes / 60) + (totalSeconds / 3600)
}

fun displayResults()
{
    println("****************** RESULTS ******************")
    println("*********************************************")
    println(" - Total No. calls: $totalCalls")
    println(" - Total call time: ${DecimalFormat("#.0").format(totalTime)} hours")
    println("*********************************************")
}

