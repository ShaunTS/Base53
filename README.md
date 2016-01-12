# Base53
Simple utility for converting numerical values to strings in base 53 using a custom radix { 0 - [a-z] - [A-Z] }

         Base53 digits[0 - 52]:
          0,
          a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z,
          A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z


         eg:
          (0, a, b, c, A, B, C) = (0, 1, 2, 3, 27, 28, 29)

          53 (base 10) = "a0" (base 53)
  
  Requires SBT (Scala Build Tool)
  
  To run corresponding unit tests, navigate to the project directory and type 'sbt testOnly'.
