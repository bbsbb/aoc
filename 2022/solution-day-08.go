package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func createForest(filePath string) [][]uint64 {
	var input []string
	f, _ := os.Open(filePath)
	fileScanner := bufio.NewScanner(f)
	fileScanner.Split(bufio.ScanLines)
	for fileScanner.Scan() {
		input = append(input, fileScanner.Text())
	}
	forest := make([][]uint64, len(input))

	for row, line := range input {
		forest[row] = make([]uint64, len(line))
		for column, c := range strings.Split(line, "") {
			forest[row][column], _ = strconv.ParseUint(c, 10, 64)
		}
	}
	return forest
}

func one(m [][]uint64) uint64 {
	result := make([][]bool, len(m))
	for idx := range m {
		result[idx] = make([]bool, len(m[0]))
	}

	endY := len(m) - 1
	endX := len(m[0]) - 1

	for i := 1; i < endY; i++ {
		leftMax := m[i][0]
		rightMax := m[i][endX]

		for j := 1; j < endX; j++ {
			if m[i][j] > leftMax {
				result[i][j] = true
				leftMax = m[i][j]
			}

			if m[i][endX-j] > rightMax {
				result[i][endX-j] = true
				rightMax = m[i][endX-j]
			}
		}
	}

	for j := 1; j < endX; j++ {
		topMax := m[0][j]
		bottomMax := m[endY][j]

		for i := 1; i < endY; i++ {
			if m[i][j] > topMax {
				result[i][j] = true
				topMax = m[i][j]
			}

			if m[endY-i][j] > bottomMax {
				result[endY-i][j] = true
				bottomMax = m[endY-i][j]
			}
		}
	}

	visible := (len(m) + len(m[0]) - 2) * 2
	for i := 1; i < len(result)-1; i++ {
		for j := 1; j < len(result[i])-1; j++ {
			if result[i][j] {
				visible = visible + 1
			}
		}
	}

	return uint64(visible)
}

func scenic(m [][]uint64, i, j int) int {
	ld := 0
	for left := j - 1; left > -1; left-- {
		ld = ld + 1
		if m[i][left] >= m[i][j] {
			break
		}
	}

	rd := 0
	for right := j + 1; right < len(m[i]); right++ {
		rd = rd + 1
		if m[i][right] >= m[i][j] {
			break
		}
	}

	td := 0
	for top := i - 1; top > -1; top-- {
		td = td + 1
		if m[top][j] >= m[i][j] {
			break
		}
	}

	bd := 0
	for bottom := i + 1; bottom < len(m); bottom++ {
		bd = bd + 1
		if m[bottom][j] >= m[i][j] {
			break
		}
	}

	fmt.Printf("For %d %d it is %d %d %d %d\n", i, j, ld, rd, td, bd)

	return ld * rd * td * bd
}

func two(m [][]uint64) int {
	result := make([][]bool, len(m))
	for idx := range m {
		result[idx] = make([]bool, len(m[0]))
	}
	score := 1
	for i := 0; i < len(m); i++ {
		for j := 0; j < len(m[0]); j++ {
			treeScore := scenic(m, i, j)
			if treeScore > score {
				score = treeScore
			}
		}
	}
	return score
}

func main() {
	input := createForest("input-day-08.txt")

	// 1859
	fmt.Println(one(input))
	// 332640
	fmt.Println(two(input))
}
