package connectfour

var rows = 0
var columns = 0
const val firstSymbol = "|o"
const val secondSymbol = "|*"
const val countSimbol = 4

// запрос на выбор какую игру играть
fun setTheBoard(firstName: String, secondName: String) {
    println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")

    val regex = Regex("""\s*\d+\s*[xX]\s*\d+\s*""")

    val enter = readLine()?.trim()
    if (enter.isNullOrEmpty()) {
        rows = 6
        columns = 7
    } else {

        val valueResult = regex.matches(enter)

        if (valueResult) {

            val enterResult = enter.split("""\s*[xX]\s*""".toRegex())
            val rows_setTheBoard = enterResult[0].trim()
            val columns_setTheBoard = enterResult[1].trim()

            rows = rows_setTheBoard.toInt()
            columns = columns_setTheBoard.toInt()

            when {
                rows !in 5..9 -> {
                    println("Board rows should be from 5 to 9")
                    setTheBoard(firstName, secondName)
                }
                columns !in 5..9 -> {
                    println("Board columns should be from 5 to 9")
                    setTheBoard(firstName, secondName)
                }
            }
        } else {
            println("Invalid input")
            setTheBoard(firstName, secondName)
        }
    }
}

//отображение игрового поля
fun seeTheBoard(placeGame: MutableList<MutableList<String>>) {

    for (i in 1..columns) {
        print(" $i")
    }
    println("")
    for (i in 0 until rows) {
        for (j in 0 until columns) {
            print(placeGame[i][j])
        }
        println("|")
    }
    for (i in 1..columns * 2 + 1) {
        print("=")
    }
    println()
}

//процесс игры с заменой игрока
fun playGame(
    firstName: String,
    firstSymbol: String,
    secondName: String,
    secondSymbol: String,
    placeGame: MutableList<MutableList<String>>
): String {
    var currentPlayer = firstName
    var currentSymbol = firstSymbol

    while (true) {
        println("$currentPlayer's turn:")
        var motion = readLine()

        while (motion != null && motion != "end") {
            val column: Int? = motion.toIntOrNull()

            if (column == null) {
                println("Incorrect column number\n$currentPlayer's turn:")
            } else if (column !in 1..placeGame[0].size) {
                println("The column number is out of range (1 - ${placeGame[0].size})\n$currentPlayer's turn:")
            } else {
                var columnFull = true
                for (i in placeGame.lastIndex downTo 0) {
                    if (placeGame[i][column - 1] != firstSymbol && placeGame[i][column - 1] != secondSymbol) {
                        placeGame[i][column - 1] = currentSymbol
                        columnFull = false
                        break
                    }
                }

                if (columnFull) {
                    println("Column $column is full\n$currentPlayer's turn:")
                } else {
                    seeTheBoard(placeGame)
                    break
                }
            }
            motion = readLine()
        }

        if (checkHorizontal(placeGame, currentSymbol) || checkVertical(placeGame, currentSymbol) || checkDiagonal(
                placeGame,
                currentSymbol
            )
        ) {
            return currentPlayer
        } else if (isBoardFull(placeGame)) {
            println("It is a draw")
            return "draw"
        }

        if (motion == "end") {
            println("Game over!")
            return "end"
        }

        currentPlayer = if (currentPlayer == firstName) secondName else firstName
        currentSymbol = if (currentSymbol == firstSymbol) secondSymbol else firstSymbol
    }
}

// проверка совпадений по горизонтали
fun checkHorizontal(placeGame: MutableList<MutableList<String>>, simbol: String): Boolean {
    for (rows in placeGame.indices) {
        for (columns in 0..placeGame[rows].size - countSimbol) {
            var match = true
            for (i in 0 until countSimbol) {
                if (placeGame[rows][columns + i] != simbol) {
                    match = false
                    break
                }
            }
            if (match) {
                return true
            }
        }
    }
    return false
}

// проверка совпадений по вертикали
fun checkVertical(placeGame: MutableList<MutableList<String>>, simbol: String): Boolean {
    for (columns in placeGame[0].indices) {
        for (rows in 0 until placeGame.size - countSimbol + 1) {
            var match = true
            for (i in 0 until countSimbol) {
                if (placeGame[rows + i][columns] != simbol) {
                    match = false
                    break
                }
            }
            if (match) {
                return true
            }
        }
    }
    return false
}

//проверка сопадений по диагонали
fun checkDiagonal(placeGame: MutableList<MutableList<String>>, simbol: String): Boolean {
    for (rows in 0..placeGame.size - countSimbol) {
        for (columns in 0..placeGame[rows].size - countSimbol) {
            var match = true
            for (i in 0 until countSimbol) {
                if (placeGame[rows + i][columns + i] != simbol) {
                    match = false
                    break
                }
            }
            if (match) {
                return true
            }
        }
    }
    for (rows in countSimbol - 1 until placeGame.size) {
        for (columns in 0..placeGame[rows].size - countSimbol) {
            var match = true
            for (i in 0 until countSimbol) {
                if (placeGame[rows - i][columns + i] != simbol) {
                    match = false
                    break
                }
            }
            if (match) {
                return true
            }
        }
    }
    return false
}

// проверка на заполненность
fun isBoardFull(placeGame: MutableList<MutableList<String>>): Boolean {
    for (row in placeGame) {
        if (row.contains("| ")) {
            return false
        }
    }
    return true
}

// выбор игры: одиночная или мульти, 
fun multiPlayerGame(
    firstName: String,
    firstSymbol: String,
    secondName: String,
    secondSymbol: String,
    placeGame: MutableList<MutableList<String>>
) {
    println(
        "Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:"
    )

    var howManyGames = readLine()
    var firstPlayerScore = 0
    var secondPlayerScore = 0

    while (howManyGames != null) {
        if (howManyGames.isEmpty() || howManyGames.toIntOrNull() == 1) {
            println("$firstName VS $secondName")
            println("${placeGame.size} X ${placeGame[0].size} board")
            println("Single game")
            seeTheBoard(placeGame)
            val result = playGame(firstName, firstSymbol, secondName, secondSymbol, placeGame)

            if (result != "end") {
                println("Player $result won")
                println("Game over!")
            } else if (result == "draw") {
                firstPlayerScore++
                secondPlayerScore++
                println("Score")
                println("$firstName: $firstPlayerScore $secondName: $secondPlayerScore")
            } else if (result == "end")
                return
        } else if (howManyGames.toIntOrNull() ?: -1 > 1) {
            println("$firstName VS $secondName")
            println("${placeGame.size} X ${placeGame[0].size} board")
            println("Total ${howManyGames.toInt()} games")

            repeat(howManyGames.toInt()) { it ->
                println("Game #${it + 1}")

                val gameBoard =
                    MutableList(placeGame.size) { MutableList(placeGame[0].size) { "| " } } // Создаем новый экземпляр игровой доски для каждой игры

                seeTheBoard(gameBoard)
                val result = playGame(
                    if (it % 2 == 0) firstName else secondName,
                    if (it % 2 == 0) firstSymbol else secondSymbol,
                    if (it % 2 == 0) secondName else firstName,
                    if (it % 2 == 0) secondSymbol else firstSymbol,
                    gameBoard
                )

                if (result != "end" && result != "draw") {
                    println("Player $result won")

                    when (result) {
                        firstName -> {
                            firstPlayerScore += 2
                        }
                        secondName -> {
                            secondPlayerScore += 2
                        }
                    }
                    println("Score")
                    println("$firstName: $firstPlayerScore $secondName: $secondPlayerScore")
                } else if (result == "draw") {
                    firstPlayerScore++
                    secondPlayerScore++
                    println("Score")
                    println("$firstName: $firstPlayerScore $secondName: $secondPlayerScore")
                } else if (result == "end")
                    return
            }
            println("Game over!")
        } else {
            println("Invalid input\n" +
                    "Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:")
        }
        howManyGames = readLine()
    }
}


fun main() {
    println("Connect Four")
    println("First player's name:")
    val firstName = readln()
    println("Second player's name:")
    val secondName = readln()

    setTheBoard(firstName, secondName)

    val placeGame = MutableList(rows) { MutableList(columns) { "| " } }

    multiPlayerGame(firstName, firstSymbol, secondName, secondSymbol, placeGame)
}

