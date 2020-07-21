using System;
using System.Linq;
using System.IO;
using System.Text;
using System.Collections;
using System.Collections.Generic;

/**
 * Made by Illedan for Hjerpbakk.
 **/
class MainClass
{
    private static int MYID;
    private static bool[,] Board;
    static void Main()
    {
        string[] inputs;
        inputs = Console.ReadLine().Split(' ');
        Console.Error.WriteLine("IN: " + string.Join(" ", inputs));
        int width = int.Parse(inputs[0]);
        int height = int.Parse(inputs[1]);
        int playercount = int.Parse(inputs[2]);
        int foodcount = int.Parse(inputs[3]);
        int myid = int.Parse(inputs[4]);
        Board = new bool[width, height];
        MYID = myid;

        // game loop
        while (true)
        {
            Board = new bool[width, height];
            int aliveplayers = int.Parse(Console.ReadLine());
            var food = new List<Position>();
            var snakes = new List<Snake>();
            for (int i = 0; i < aliveplayers; i++)
            {
                inputs = Console.ReadLine().Split(' ');
                Console.Error.WriteLine("IN: " + string.Join(" ", inputs));

                int id = int.Parse(inputs[0]);
                int score = int.Parse(inputs[1]);
                int size = int.Parse(inputs[2]);
                var snake = inputs[3].Split(',').Select(int.Parse).ToArray();
                var s = new Snake(id, score);
                snakes.Add(s);
                for(var j = 0; j < size; j+=2){
                    s.Positions.Add(new Position(snake[j], snake[j+1]));
                    Board[snake[j], snake[j+1]] = true;
                }
            }
            for (int i = 0; i < foodcount; i++)
            {
                inputs = Console.ReadLine().Split(' ');
                Console.Error.WriteLine("IN: " + string.Join(" ", inputs));
                int x = int.Parse(inputs[0]);
                int y = int.Parse(inputs[1]);
                food.Add(new Position(x, y));
            }

            var dx = new int[]{-1,1,0,0};
            var dy = new int[]{0,0,-1,1};
            var str = new string[]{"W", "E", "N",  "S"};
            var me = snakes.First(s => s.Id == MYID);
            var head = me.Positions.First();
            for(var i = 0; i < 4; i++){
                var x = head.X+dx[i];
                var y = head.Y+dy[i];
                if (x < 0 || y < 0 || x >= width || y >= height){
                    continue;
                }
                if (Board[x,y]) continue;
                Console.WriteLine(str[i]);
                break;
            }
        }
    }
}

class Snake{
    public int Score, Id;
    public List<Position> Positions = new List<Position>();
    public Snake(int id, int score){
        Score = score;
        Id = id;
    }
}

class Position{
    public int X, Y;
    public Position(int x, int y){
        X = x;
        Y = y;
    }
}