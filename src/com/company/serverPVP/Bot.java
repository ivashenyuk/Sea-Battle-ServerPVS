package com.company.serverPVP;

import java.util.Random;

public class Bot {
    private static boolean[][] aldPosition = new boolean[10][10];
    private Direction direction = Direction.RIGHT;
    int countStep = 0;
    private static boolean checkDirection = true;
    private static DataBase dataBase = new DataBase();
    static String complexity = "easy";
    int[][] statistic = this.GetStatistic();

    public Bot() {
        SetShips();
    }

    public void Step() {
        if (complexity.equals("easy")) {
            Easy();
        } else if (complexity.equals("hard")) {
            Hard();
        }
        else if(complexity.equals("impossible")){
            Impossible();
        }
    }

    private boolean CheckPlaceForShip(int indexX, int indexY) {
        if (indexX >= 0 && indexY >= 0 && indexX < 10 && indexY < 10)
            if (Server.placeOfBattleEnemy[indexX][indexY].isHere)
                return true;
        return false;
    }

    private int CountStep() {
        int count = 0;
        for (int i = 0; i < 10; i++) {
            for (int y = 0; y < 10; y++) {
                if (aldPosition[i][y])
                    count++;
            }
        }
        return count;
    }

    private void SetShips() {
        Random random = new Random();
        int xPosition = 0;
        int yPosition = 0;
        for (int i = 0; i < Server.playerUser.allShips; i++) {
            xPosition = random.nextInt(10);
            yPosition = random.nextInt(10);

            if (checkDirection) {
                int directions = random.nextInt(5);
                switch (directions) {
                    case 0:
                        direction = Direction.UP;
                        break;
                    case 1:
                        direction = Direction.RIGHT;
                        break;
                    case 2:
                        direction = Direction.DOWN;
                        break;
                    case 3:
                        direction = Direction.LEFT;
                        break;
                }
            }
            if (!SetShip(xPosition, yPosition)) {
                i--;
                checkDirection = false;
                GetNextDirection();
            } else {
                checkDirection = true;
            }
        }
    }

    private void GetNextDirection() {
        int index = direction.ordinal();
        int nextIndex = index + 1;
        Direction[] dir = Direction.values();
        nextIndex %= dir.length;
        direction = dir[nextIndex];
    }

    private boolean SetShip(int _xPosition, int _yPosition) {
        //-------------- For One Ship
        if (Server.playerUser.countShip1 > 0) {

            if (CheckPlaceForShip(_xPosition, _yPosition, 1))
                return false;
            if (!CheckPlaceForShip(_xPosition, _yPosition)) {
                Server.playerUser.countShip1--;
                SetPositionShip(_xPosition, _yPosition, 1);
            }
        }
        //-------------- For Two Ship
        else if (Server.playerUser.countShip2 > 0) {

            if (CheckPlaceForShip(_xPosition, _yPosition, 2))
                return false;

            if (!CheckPlaceForShip(_xPosition, _yPosition)) {
                Server.playerUser.countShip2--;
                SetPositionShip(_xPosition, _yPosition, 2);
            }
        }
        //-------------- For Three Ship
        else if (Server.playerUser.countShip3 > 0) {

            if (CheckPlaceForShip(_xPosition, _yPosition, 3))
                return false;

            if (!CheckPlaceForShip(_xPosition, _yPosition)) {
                Server.playerUser.countShip3--;
                SetPositionShip(_xPosition, _yPosition, 3);
            }
        }
        //-------------- For Four Ship
        else if (Server.playerUser.countShip4 > 0) {

            if (CheckPlaceForShip(_xPosition, _yPosition, 4))
                return false;

            if (!CheckPlaceForShip(_xPosition, _yPosition)) {
                Server.playerUser.countShip4--;
                SetPositionShip(_xPosition, _yPosition, 4);
            }
        }
        return true;
    }

    private boolean CheckPlaceForShip(int indexX, int indexY, int sizeShip) {
        Coord coordBegin = new Coord(indexX, indexY);
        Coord coordEnd = null;

        if (direction == Direction.RIGHT) {
            coordEnd = new Coord(coordBegin.x + sizeShip, coordBegin.y);
        } else if (direction == Direction.LEFT) {
            coordEnd = coordBegin;
            coordBegin = new Coord(coordBegin.x - sizeShip, coordBegin.y);
        } else if (direction == Direction.DOWN) {
            coordEnd = new Coord(coordBegin.x, coordBegin.y + sizeShip);
        } else if (direction == Direction.UP) {
            coordEnd = coordBegin;
            coordBegin = new Coord(coordBegin.x, coordBegin.y - sizeShip);
        }

        if (coordBegin != null && coordEnd != null) {
            for (int x = coordBegin.x - 1; x <= coordEnd.x + 1; x++) {
                for (int y = coordBegin.y - 1; y <= coordEnd.y + 1; y++) {
                    if (x >= 0 && y >= 0 && x <= 9 && y <= 9) {
                        if (Server.placeOfBattleUser[x][y].isHere)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private void SetPositionShip(int _x, int _y, int sizeShip) {
        //_x = 7;
        //_y = 1;
        int countSetShip = 0;
        if (direction == Direction.RIGHT) {
            //System.out.println("RIGHT");
            for (int x = 0; x < sizeShip; x++) {
                int indexY = _y;
                if (_x + x > 9) {
                    countSetShip++;
                    int indexX = _x - countSetShip;
                    countSetShip++;
                    Server.placeOfBattleUser[indexX][indexY].coord = new Coord(indexX + 2, indexY + 6);
                    Server.placeOfBattleUser[indexX][indexY].isHere = true;
                } else {
                    int indexX = _x + x;
                    Server.placeOfBattleUser[indexX][indexY].coord = new Coord(indexX + 2, indexY + 6);
                    Server.placeOfBattleUser[indexX][indexY].isHere = true;
                }
            }
        } else if (direction == Direction.LEFT) {
            //System.out.println("LEFT");
            int indexY = _y;
            for (int x = 0; x < sizeShip; x++) {
                if (_x - x < 0) {
                    countSetShip++;
                    int indexX = _x + countSetShip;
                    Server.placeOfBattleUser[indexX][indexY].coord = new Coord(indexX + 2, indexY + 6);
                    Server.placeOfBattleUser[indexX][indexY].isHere = true;
                } else {
                    int indexX = _x - x;
                    Server.placeOfBattleUser[indexX][indexY].coord = new Coord(indexX + 2, indexY + 6);
                    Server.placeOfBattleUser[indexX][indexY].isHere = true;
                }
            }
        } else if (direction == Direction.DOWN) {
            //System.out.println("DOWN");
            for (int y = 0; y < sizeShip; y++) {
                if (_y + y > 9) {
                    countSetShip++;
                    int indexX = _x;
                    int indexY = _y - countSetShip;

                    Server.placeOfBattleUser[indexX][indexY].coord = new Coord(indexX + 2, indexY + 6);
                    Server.placeOfBattleUser[indexX][indexY].isHere = true;
                } else {
                    int indexX = _x;
                    int indexY = _y + y;

                    Server.placeOfBattleUser[indexX][indexY].coord = new Coord(indexX + 2, indexY + 6);
                    Server.placeOfBattleUser[indexX][indexY].isHere = true;
                }
            }
        } else if (direction == Direction.UP) {
            //System.out.println("UP");
            for (int y = 0; y < sizeShip; y++) {
                if (_y - y < 0) {
                    countSetShip++;
                    int indexX = _x;
                    int indexY = _y + countSetShip;
                    //System.out.println(indexX + ":" + indexY);
                    Server.placeOfBattleUser[indexX][indexY].coord = new Coord(indexX + 2, indexY + 6);
                    Server.placeOfBattleUser[indexX][indexY].isHere = true;
                } else {
                    int indexX = _x;
                    int indexY = _y - y;
                    //System.out.println(indexX + ":" + indexY);
                    Server.placeOfBattleUser[indexX][indexY].coord = new Coord(indexX + 2, indexY + 6);
                    Server.placeOfBattleUser[indexX][indexY].isHere = true;
                }
            }
        }
    }

    public static void Learning() {
        dataBase.Learning();
    }

    private int[][] GetStatistic() {
        return dataBase.GetStatistic();
    }

    private static int k = 0;

    private int[] GetMaxValue(int[][] Mass) {
        int[] returnValue = new int[]{0, 0};
        int max = Mass[0][0];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (max < Mass[i][j]) {
                    max = Mass[i][j];
                    System.out.println(i+":"+j);
                    returnValue[0] = i;
                    returnValue[1] = j;
                }
            }
        }
        if (max == Mass[0][0])
            k++;
        if (k > 0)
            return new int[]{-1, -1};
        return returnValue;
    }

    private void Easy() {
        Random random = new Random();
        countStep = 1;
        int xPosition = 0;
        int yPosition = 0;
        for (int onemoreStep = 0; onemoreStep < 1; onemoreStep++) {
            for (int i = 0; i < 1; i++) {
                if (countStep > 1) {
                    int direct = random.nextInt(5);
                    switch (direct) {
                        case 0:
                            if (xPosition + 1 < 10)
                                xPosition++;
                            else
                                xPosition -= countStep + 1;
                            break;
                        case 1:
                            if (xPosition - 1 >= 0)
                                xPosition--;
                            else
                                xPosition += countStep + 1;
                            break;
                        case 2:
                            if (yPosition + 1 < 10)
                                yPosition++;
                            else
                                yPosition -= countStep + 1;
                            break;
                        case 3:
                            if (yPosition - 1 >= 0)
                                yPosition--;
                            else
                                yPosition += countStep + 1;
                            break;
                    }
                } else {
                    xPosition = random.nextInt(10);
                    yPosition = random.nextInt(10);
                }
                if (!aldPosition[xPosition][yPosition])
                    aldPosition[xPosition][yPosition] = true;
                else if (CountStep() <= 100) --i;
                else break;
            }

            if (!CheckPlaceForShip(xPosition, yPosition)) {
                Server.placeOfBattleEnemy[xPosition][yPosition].slip = true;
            } else {
                Server.placeOfBattleEnemy[xPosition][yPosition].beaten = true;
                countStep++;
                --onemoreStep;
            }
        }
    }

    private void Hard() {
        Random random = new Random();
        int xPosition = 0;
        int yPosition = 0;
        for (int onemoreStep = 0; onemoreStep < 1; onemoreStep++) {
            for (int i = 0; i < 1; i++) {
                int[] res = GetMaxValue(statistic);
                xPosition = res[0];
                yPosition = res[1];
               // System.out.println(xPosition + ":" + yPosition);


                if (CountStep() > 100 || xPosition == -1 || yPosition == -1) {
                    Easy();
                    break;
                }
                statistic[xPosition][yPosition] = 0;
                if (!aldPosition[xPosition][yPosition])
                    aldPosition[xPosition][yPosition] = true;
                else if (CountStep() <= 100) --i;
                else break;
            }
if(xPosition == -1 || yPosition == -1)
    continue;
            if (!CheckPlaceForShip(xPosition, yPosition)) {
                Server.placeOfBattleEnemy[xPosition][yPosition].slip = true;
            } else {
                Server.placeOfBattleEnemy[xPosition][yPosition].beaten = true;
                --onemoreStep;
            }
        }
    }
    private void Impossible(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(Server.placeOfBattleEnemy[i][j].isHere)
                    Server.placeOfBattleEnemy[i][j].beaten = true;
            }
        }
    }
}
