// See https://aka.ms/new-console-template for more information
//
// Console.WriteLine("Hello, World!");

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using implementation.Implementation;

namespace implementation
{
    public class Program
    {
        static void Main()
        {
            Stopwatch stopwatch = new Stopwatch();
            var hosts = new List<string>
            {
                "www.reqbin.com/echo",
                "www.cs.ubbcluj.ro/~ilazar/ma/",
                "www.cs.ubbcluj.ro/~forest",
            };

            // Console.WriteLine("-------------------------");
            // Console.WriteLine("DIRECT CALLBACK");
            // stopwatch.Start();
            // try
            // {
            //     DirectCallback.Run(hosts);
            // }
            // catch (Exception ex)
            // {
            //     Console.WriteLine("Error: " + ex.Message);
            // }
            // stopwatch.Stop();
            // TimeSpan directCallbackTime = stopwatch.Elapsed;

            
            
            // Console.WriteLine("-------------------------");
            // Console.WriteLine("TASKS");
            // stopwatch.Restart();
            // try
            // {
            //     TaskMechanism.Run(hosts);
            // }
            // catch (Exception ex)
            // {
            //     Console.WriteLine("Error: " + ex.Message);
            // }
            // stopwatch.Stop();
            // TimeSpan taskMechanismTime = stopwatch.Elapsed;
            
            
            //
            Console.WriteLine("-------------------------");
            Console.WriteLine("ASYNC/AWAIT MECHANISM");
            stopwatch.Restart();
            try
            {
                AsyncTaskMechanism.Run(hosts);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.Message);
            }
            stopwatch.Stop();
            TimeSpan asyncTaskMechanismTime = stopwatch.Elapsed;

            //Console.WriteLine("Direct callback: {0}", directCallbackTime);
             //Console.WriteLine("Task mechanism: {0}", taskMechanismTime);
             //Console.WriteLine("Async/await task mechanism: {0}", asyncTaskMechanismTime);
        }
    }
}