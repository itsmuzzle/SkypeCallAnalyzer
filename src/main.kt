import java.io.File
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun main(args: Array<String>)
{
    /** The chat log file that we will loop through */
    val inputFile: File = File("C:\\Users\\lewis\\IdeaProjects\\SkypeCallAnalyzer\\src\\file.txt")

    /** Scanner to loop through the file */
    val scanner = Scanner(inputFile)

    /** We use this string to determine if a line from the Skype output log was a call or not. Only calls > 0's
     * will contain this string. Failed calls will be omitted from the results */
    val stringToCheck = "Call ended, duration"

    /** Pattern for checking if the time is in the format hh:mm:ss.
     * The call duration format from the Skype logs can be in either "mm:ss" or "hh:mm:ss" so we will
     * use this to separate all lines that match this pattern. Once separated it will be easier
     * to add up the total duration later */
    val pattern: Pattern = Pattern.compile("([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]:([0-5][0-9]|[6][0])")

    /** The total number of valid (not failed) calls found in the log file */
    var totalCalls: Int = 0

    /** The total duration of all Skype calls in the log*/
    var totalDuration: Double = 0.0

    /** Loop through the chat log until we reach the end */
    while (scanner.hasNextLine())
    {
        /** Return the current line of the chat log */
        var currentLine: String = scanner.nextLine()

        /** Contains the number of hours from a single line in the log*/
        var hours: Int

        /** Contains the number of minutes from a single line in the log*/
        var minutes: Int

            /** If the current line contains our set string then it must be a call */
            if (currentLine.contains(stringToCheck))
            {
                /** We found a call so update the total calls counter */
                totalCalls++

                /** Print the current line showing the call in question */
                println(currentLine)

                /** Trim the current line to contain only the call duration */
                currentLine = currentLine.substring(47, currentLine.lastIndexOf(" "))

                /** Use matcher to check if our call duration matches the given pattern.
                 * If a match is found, it means that our duration is in the format hh:mm:ss,
                 * so we need to account for hours as well as just minutes and seconds */
                val matcher: Matcher = pattern.matcher(currentLine)
                if (matcher.find())
                {
                    /** Replace the first ":" with "." then remove everything after index 4,
                     * which basically just removes the seconds. There is a better way to do
                     * this i know but i'm tired and it works unless you have calls that
                     * last 10 hours or more, at which point the code will crash. I don't
                     * so i'ts fine for now until I can think of some way better */
                     // TODO: Fix this bad code
                    currentLine = currentLine.replaceFirst(":", ".").substring(0,4)

                    /** Get the hours from the first index
                     * See above about bad code blah blah **/
                    hours = Integer.parseInt(currentLine.substring(0, 1))

                    /** Get the minutes starting from index 2
                     * See above about bad code blah blah **/
                    minutes = Integer.parseInt(currentLine.substring(2))
                }
                else
                {
                    /** If the duration has no hours present then we only
                     * have to worry about the minutes*/
                    hours = 0
                    minutes = Integer.parseInt(currentLine.substring(0, 2))
                }
                totalDuration += (hours * 60) + minutes
            }
    }
    scanner.close()
    println("***** [RESULTS] *****")
    println("Total Calls Made: $totalCalls")
    println("Total Call Duration (Minutes): $totalDuration")
    println("Total Call Duration (Hours): ${totalDuration / 60}")
}