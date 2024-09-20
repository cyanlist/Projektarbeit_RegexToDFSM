# RegexToDFSM

## Project Description
This project provides a graphical application that allows users to convert regular expressions into deterministic finite automata (DFSM). The main goal of the software is to demonstrate the transformation of regular expressions step-by-step, providing transparency and a deeper understanding of the algorithms and concepts. The software is designed for students studying theoretical computer science as well as researchers and developers working with finite automata.

![regex_to_dfsm.png](src/resources/regex_to_dfsm.png)

## Features
- **Input of regular expressions**: Users can enter regular expressions through an input field.
- **Visualization**: The resulting finite automaton is shown as a state diagram.
- **Step-by-Step transformation**: The conversion of a regular expression into a finite automaton is displayed in stages, with each intermediate step being visualized.
- **Interactive walkthrough**: Users can go through the various stages of transformation to understand each step.

## Requirements
- **Java**: The application is developed in Java and requires Java Runtime Environment (JRE) version 8 or later.
- **Maven**: Maven is used for build management. All necessary dependencies are managed through Maven, so there is no need to manually download them.

## Installation and Execution
1. **Install Java**: Ensure that Java 8 or a later version is installed on your system.
2. **Clone the project**: Clone the project into your local directory:
    ```bash
    git clone https://github.com/cyanlist/Projektarbeit_RegexToDFSM.git
    ```
3. **Use Maven**: Build the project with maven.
4. **Run the application**: After a successful build, start the program in `main.java`.

## Project Structure
The project is modular and follows the principle of single responsibility. Each class has a specific, well-defined task to ensure the maintainability and extensibility of the code.

- **GUI**: The graphical user interface (GUI) is implemented using Java Swing and AWT, where the user interaction occurs.
- **Logic**: The core logic for transforming regular expressions into finite automata is located in the `logic/postfix` package. This includes the steps from validating the regular expression, converting it to postfix notation, and generating finite state machines.
- **Automata**: Classes responsible for representing and manipulating finite automata are found in the `logic/finiteStateMachine` package.

## Technologies and Libraries Used
- **Swing & AWT**: Used for building the user interface and visualizing state machines.
- **JUnit**: Unit testing framework for testing core logic components.
- **FlatLaf**: Provides a modern and flat look-and-feel for the GUI.
- **Maven**: Build and dependency management tool.

## Developer Notes
- **Maven**: All dependencies are managed via Maven. Configuration is handled through the `pom.xml` file, so no manual library installation is required.

## Platform Compatibility
The project is platform-independent and runs on both Windows and Linux. Although a macOS version has not been tested, the application should run on Apple devices provided Java is installed.

## License
This project is licensed under the MIT License.  

---

For questions or contributions, feel free to reach out via the GitHub repository. Feedback and suggestions are always welcome!
