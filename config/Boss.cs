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
                for(var j = 0; j < size*2; j+=2){
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
            var bestScore = 1000.0;
            var best = str[0];
            for(var i = 0; i < 4; i++){
                var x = head.X+dx[i];
                var y = head.Y+dy[i];
                if (x < 0 || y < 0 || x >= width || y >= height){
                    continue;
                }
                if (Board[x,y]) continue;
                var p = new Position(x, y);
                var closeFood = food.OrderBy(f => f.Dist2(p)).First();
                var score = closeFood.Dist2(p)+ 0.0;
                foreach(var f in food){
                    //score += f.Dist2(p)*0.0000001;
                }
                for(var j = 0; j < 4;j++){
                    var xx = dx[j]+x;
                    var yy = dy[j]+y;
                     if (xx < 0 || yy < 0 || xx >= width || yy >= height){
                         score -=4;
                        continue;
                    }
                    if (Board[xx,yy]) continue;
                    score-=5;
                }
                if(score < bestScore){
                    best = str[i];
                    bestScore = score;
                }
            }

            Console.WriteLine(best);
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

    public int Pow(int x) => x*x;
    public int Dist2(Position p2) => Math.Abs(p2.X-X) + Math.Abs(p2.Y-Y);
}