# Genetic Algorithms for Efficient Inventory Redistribution

## Abstract

This project addresses the challenge of optimizing the restocking process in warehousing operations using genetic algorithms (GAs). The focus is on minimizing the time required to distribute inventory items across various sections of a facility, considering the impact of item weight on the speed of the restocker.

## Key Features

- Virtual inventory generation with random quantities and predefined weights
- Simulated warehouse environment with randomly positioned sections
- Genetic algorithm implementation for path optimization
- Consideration of item weight impact on restocking speed
- Comparison with baseline algorithms (nearest neighbor and weight prioritization)

## Methodology

- Simulation setup: 30 unique items, 15 distinct populations, 500 entities per population
- Fitness evaluation based on total time required to complete the restocking path
- Speed calculation incorporating current weight carried
- Selection methods tested: Pool Selection, Roulette Wheel Selection, Tournament Selection

## Results

- The genetic algorithm consistently outperformed baseline algorithms:
  - 18.79% improvement over the nearest neighbor algorithm
  - 112.05% improvement over the weight prioritization algorithm
- Tournament selection emerged as the most effective selection method

## Future Work

- Incorporate physical obstacles in the warehouse environment
- Explore dynamic inventory levels
- Investigate hybrid algorithms combining GA with other optimization techniques
