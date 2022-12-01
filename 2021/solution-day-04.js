const fs = require('fs')

const input = fs.readFileSync("input-day-04.txt").toString().split("\n")
const drawInput = input[0].split(",").map(d => parseInt(d, 10));

const {boards: boardInput} = input.slice(2).reduce(
    (acc, line) => {
        const {boards, current} = acc;
        if (line === "") {
            return {
                boards: [...boards, current],
                current: [],
            };
        }
        return {
            boards,
            current: [
                ...current,
                line.trim()
                    .replace(/\s{2,}/g, ' ')
                    .split(" ")
                    .map(d => {
                        return {
                            number: parseInt(d, 10),
                            marked: false
                        }
                    })
            ]
        }
    },
    {
        boards: [],
        current: [],
    }
);

const markBoard = (number, board) => {
    return board.map(row => row.map(candidate => {
        return {
            ...candidate,
            marked: candidate.marked || candidate.number === number,
        }
    }));
}

const checkBoard = (board) => {
    let winningRow = [];
    let winningColumn = [];
    for (let column = 0; column < board.length; column++) {
        winningRow = [];
        winningColumn = [];

        for (let row = 0; row < board.length; row++){
            if (board[row][column].marked === true) {
                winningColumn.push(board[row][column].number)
            }
            if (board[column][row].marked === true) {
                winningRow.push(board[column][row].number)
            }
        }

        if (winningRow.length === board.length) {
            return board;
        }
        if (winningColumn.length === board.length) {
            return board;
        }
    }

    return null
}


const winningPayout = (draw, boards, direction = 'first') => {
    let winner = null;
    let resultBoards = boards;
    let idx = 0
    let drawn = null;
    while (winner === null && draw.length > 0) {
        drawn = draw.shift();
        resultBoards = resultBoards.map(b => markBoard(drawn, b))
        if (direction === 'first') {
            winner = resultBoards.reduce(
                (winningBoard, current) => winningBoard || checkBoard(current),
                null,
            );
        } else {
            if (resultBoards.length > 1) {
                resultBoards = resultBoards.filter(b => checkBoard(b) === null)
            } else if (checkBoard(resultBoards[0])){
                winner = resultBoards[0]
            }
        }
    }

    if (winner) {
        return drawn * winner.reduce((unmarkedSum, boardRow) => {
            return unmarkedSum + boardRow.reduce((s, c) => s + (c.marked ? 0 : c.number),0)
        }, 0)
    }
    return null;
}


//console.log(winningPayout(drawInput, boardInput))
console.log(winningPayout(drawInput, boardInput, 'last'))
