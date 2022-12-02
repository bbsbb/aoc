package main

import "fmt"
import "math"
import "strings"
import "strconv"
import "sort"

func sortedCrabs(input string) []float64 {
	crabs := strings.Split(input, ",")
	d := make([]float64, len(crabs))
	for i, c := range crabs {
		p, _ := strconv.ParseFloat(c, 64)
		d[i] = p
	}
	sort.Float64s(d)
	return d
}

func fuelCost(distance float64) float64 {
	return math.Floor(distance * (distance + 1) / 2)
}

func optimalFuel(crabs []float64, scienceCrab bool) float64 {
	optimalFuelSpent := math.MaxFloat64

	for potential := crabs[0]; potential <= crabs[len(crabs) - 1]; potential++ {
		fuelForTarget := 0.0
		for _, crab := range crabs {
			distance := math.Abs(crab - potential)
			if scienceCrab {
				fuelForTarget = fuelForTarget + fuelCost(distance)
			} else {
				fuelForTarget = fuelForTarget + distance
			}
		}
		if fuelForTarget < optimalFuelSpent {
			optimalFuelSpent = fuelForTarget
		}
	}
	return optimalFuelSpent
}


func main() {
	input := "1101,1,29,67,1102,0,1,65,1008,65,35,66,1005,66,28,1,67,65,20,4,0,1001,65,1,65,1106,0,8,99,35,67,101,99,105,32,110,39,101,115,116,32,112,97,115,32,117,110,101,32,105,110,116,99,111,100,101,32,112,114,111,103,114,97,109,10,47,540,154,191,254,437,365,556,499,283,685,687,673,594,21,346,283,52,284,260,353,453,93,523,236,352,1221,433,89,738,1434,368,712,507,256,813,160,654,546,1101,54,56,636,89,58,259,4,696,25,8,826,80,325,247,409,706,241,273,849,137,1674,1180,354,131,273,220,1087,397,14,533,375,17,371,89,137,485,428,520,6,430,713,66,53,501,8,263,290,972,825,48,648,226,53,113,1300,290,302,1401,158,576,120,27,746,371,267,543,44,234,238,1278,441,991,1335,118,372,507,1237,285,567,192,626,461,125,317,461,574,38,655,619,100,310,141,125,455,1161,62,335,97,345,104,680,391,202,626,20,237,529,19,675,201,462,547,657,832,203,220,1184,1057,109,54,549,285,336,870,797,403,179,526,383,788,305,713,77,21,618,809,104,765,540,53,154,868,295,703,354,63,726,126,3,256,397,133,1549,29,1200,416,992,526,1361,226,417,125,509,40,283,380,195,1388,548,256,20,13,163,294,242,1023,761,198,798,1030,189,509,588,38,570,1257,1029,953,1014,636,167,122,180,198,76,570,117,344,1372,638,101,5,514,67,37,123,28,266,1064,1092,317,55,23,1437,100,1255,126,533,30,421,695,385,151,892,2,103,465,147,901,311,373,763,599,625,26,18,2,1391,277,584,317,879,375,638,1131,82,526,1310,255,128,204,893,4,65,116,18,293,1297,382,349,59,731,225,819,1563,1075,867,66,53,579,891,1321,819,61,18,164,1232,18,815,481,336,450,43,200,152,442,302,65,1185,363,418,619,2,176,305,966,291,1760,1358,581,51,18,386,540,60,4,1295,76,149,401,235,149,219,102,896,1135,1895,382,599,300,382,260,698,1353,1306,50,307,903,297,71,1148,1540,15,444,843,1256,30,907,349,26,503,1468,19,282,203,186,1014,626,737,146,229,300,1250,83,1000,108,150,400,27,147,1219,50,62,768,328,566,202,613,1018,603,738,287,74,1120,968,485,216,1308,402,677,668,780,350,201,618,171,111,776,231,956,131,1115,205,374,15,528,183,485,366,343,190,39,353,6,67,753,283,1064,638,724,697,16,706,588,909,97,298,448,281,932,1535,78,2,0,314,318,1522,491,856,1318,217,486,893,589,1682,95,452,428,1463,426,555,271,885,1215,429,202,586,1350,17,635,181,15,486,51,8,1194,1066,24,1226,0,1093,41,652,11,1434,125,1041,196,998,1072,1449,198,33,44,1592,514,439,128,1265,215,238,63,72,830,953,329,1025,253,1955,581,387,1656,86,969,1630,400,529,80,922,47,650,110,1814,1083,750,1065,21,181,384,1151,171,1009,602,1193,2,55,151,1182,536,650,117,722,1123,1724,38,938,1539,1586,13,1,420,1070,307,721,328,671,231,205,444,1054,810,490,130,458,94,0,408,487,1583,750,556,195,1533,26,588,224,719,40,247,277,740,320,251,58,88,1433,1026,104,137,62,615,543,611,339,129,126,302,310,20,275,201,306,377,729,10,715,369,575,400,627,843,545,266,1302,253,538,239,22,47,60,21,664,1158,13,36,506,262,68,154,802,930,11,24,18,1034,312,134,1161,52,269,770,1354,1258,1087,584,858,83,435,33,1549,446,72,1612,510,39,580,696,325,46,1603,871,312,436,251,549,952,74,147,915,236,76,954,1343,137,591,90,42,1119,105,274,1177,137,1894,642,146,458,415,746,494,1017,973,512,462,730,638,475,65,22,537,237,409,1842,136,713,131,1544,8,3,363,513,645,970,1361,204,248,78,438,518,670,5,1026,297,769,508,55,17,470,223,1264,130,43,245,734,1203,1620,264,113,1781,139,1024,1176,1194,1099,528,588,13,510,251,552,438,448,1157,1233,163,1456,964,903,453,605,253,52,171,404,894,214,198,20,933,259,442,109,73,1612,463,1177,51,934,7,308,99,456,1311,647,523,146,456,585,1403,565,245,252,72,145,15,903,1197,407,159,34,88,1572,803,577,16,945,306,496,654,576,228,59,1321,628,90,1626,251,429,142,166,1246,264,184,259,1091,816,1455,12,802,1530,303,1034,99,1378,130,733,449,1897,261,194,641,42,382,1296,127,887,134,1014,310,20,449,61,99,209,1667,432,406,1357,419,644,471,172,1239,225,1239,85,170,1017,321,378,1702,549,488,9,72,410,540,299,10,6,209,24,1606,25,191,1188,8,181,1028,648,1072,392,150,268,785,274,843,495,179,41,1240,648,1045,1651,190,656,1059,281,1206,292,334,1932,97,350,236,965"
	//sampleInput := "16,1,2,0,4,2,7,1,2,14"

	crabs := sortedCrabs(input)
	fmt.Printf("%f\n", optimalFuel(crabs, false))
	fmt.Printf("%f\n", optimalFuel(crabs, true))
}