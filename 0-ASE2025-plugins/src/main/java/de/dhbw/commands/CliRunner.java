package de.dhbw.commands;

import de.dhbw.commands.detaineecommands.ListDetaineesCommand;
import de.dhbw.commands.detaineecommands.RegisterDetaineeCommand;
import de.dhbw.commands.detaineecommands.ReleaseDetaineeCommand;
import de.dhbw.commands.officercommands.ListOfficersCommand;
import de.dhbw.commands.officercommands.RegisterOfficerCommand;
import de.dhbw.commands.roomcommands.BookRoomCommand;
import de.dhbw.commands.roomcommands.CreateRoomCommand;
import de.dhbw.commands.roomcommands.ListRoomsCommand;
import de.dhbw.commands.ChangeRankCommand;
import de.dhbw.repositories.*;
import de.dhbw.services.*;
import de.dhbw.usecases.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main CLI application runner that sets up the command registry and manages the main input loop.
 */
public class CliRunner {
    private final de.dhbw.cli.CommandHandler commandHandler;
    private final Scanner scanner;

    // Repositories
    private final RoomRepository roomRepository;
    private final OfficerRepository officerRepository;
    private final DetaineeRepository detaineeRepository;
    private final InterrogationRepository interrogationRepository;
    private final MeetingRepository meetingRepository;

    // Services
    private final RoomManagementService roomManagementService;
    private final OfficerService officerService;
    private final DetaineeService detaineeService;
    private final InterrogationService interrogationService;
    private final MeetingService meetingService;

    // Use cases
    private final CreateRoomUseCase createRoomUseCase;
    private final BookRoomUseCase bookRoomUseCase;
    private final GetAllRoomsUseCase getAllRoomsUseCase;
    private final GetAllBookedRoomsUseCase getAllBookedRoomsUseCase;
    private final ChangeRankUseCase changeRankUseCase;
    private final GetAllDetaineesUseCase getAllDetaineesUseCase;
    private final RegisterDetaineeUseCase registerDetaineeUseCase;
    private final ReleaseDetaineeUseCase releaseDetaineeUseCase;
    private final InterrogateDetaineeUseCase interrogateDetaineeUseCase;
    private final ScheduleMeetingUseCase scheduleMeetingUseCase;

    /**
     * Creates a new CLI runner with all necessary dependencies.
     *
     * @param roomRepository The room repository
     * @param officerRepository The officer repository
     * @param detaineeRepository The detainee repository
     * @param interrogationRepository The interrogation repository
     * @param meetingRepository The meeting repository
     */
    public CliRunner(
            RoomRepository roomRepository,
            OfficerRepository officerRepository,
            DetaineeRepository detaineeRepository,
            InterrogationRepository interrogationRepository,
            MeetingRepository meetingRepository) {

        this.commandHandler = new de.dhbw.cli.CommandHandler();
        this.scanner = new Scanner(System.in);

        // Set up repositories
        this.roomRepository = roomRepository;
        this.officerRepository = officerRepository;
        this.detaineeRepository = detaineeRepository;
        this.interrogationRepository = interrogationRepository;
        this.meetingRepository = meetingRepository;

        // Set up services
        this.roomManagementService = new RoomManagementService(roomRepository);
        this.officerService = new OfficerService(officerRepository);
        this.detaineeService = new DetaineeService(detaineeRepository);
        this.interrogationService = new InterrogationService(
                roomRepository, officerRepository, interrogationRepository);
        this.meetingService = new MeetingService(
                roomRepository, officerRepository, meetingRepository);

        // Set up use cases
        this.createRoomUseCase = new CreateRoomUseCase(roomManagementService);
        this.bookRoomUseCase = new BookRoomUseCase(roomManagementService);
        this.getAllRoomsUseCase = new GetAllRoomsUseCase(roomManagementService);
        this.getAllBookedRoomsUseCase = new GetAllBookedRoomsUseCase(roomManagementService);
        this.changeRankUseCase = new ChangeRankUseCase(officerService);
        this.getAllDetaineesUseCase = new GetAllDetaineesUseCase(detaineeService);
        this.registerDetaineeUseCase = new RegisterDetaineeUseCase(detaineeService);
        this.releaseDetaineeUseCase = new ReleaseDetaineeUseCase(detaineeService);
        this.interrogateDetaineeUseCase = new InterrogateDetaineeUseCase(
                interrogationService, detaineeService);
        this.scheduleMeetingUseCase = new ScheduleMeetingUseCase(meetingService);

        // Register commands
        registerCommands();
    }

    /**
     * Registers all commands with the command handler.
     */
    private void registerCommands() {
        List<Command> commands = new ArrayList<>();

        // Room management commands
        Command createRoomCommand = new CreateRoomCommand(createRoomUseCase);
        Command bookRoomCommand = new BookRoomCommand(bookRoomUseCase);
        Command listRoomsCommand = new ListRoomsCommand(getAllRoomsUseCase);

        // Officer management commands
        Command registerOfficerCommand = new RegisterOfficerCommand(officerService);
        Command changeRankCommand = new ChangeRankCommand(changeRankUseCase);
        Command listOfficersCommand = new ListOfficersCommand(officerRepository);

        // Detainee management commands
        Command registerDetaineeCommand = new RegisterDetaineeCommand(registerDetaineeUseCase);
        Command listDetaineesCommand = new ListDetaineesCommand(getAllDetaineesUseCase);
        Command releaseDetaineeCommand = new ReleaseDetaineeCommand(releaseDetaineeUseCase);

        // Activity management commands
        Command scheduleInterrogationCommand = new ScheduleInterrogationCommand(interrogateDetaineeUseCase);
        Command scheduleMeetingCommand = new ScheduleMeetingCommand(scheduleMeetingUseCase);

        // Add all commands to the list
        commands.add(createRoomCommand);
        commands.add(bookRoomCommand);
        commands.add(listRoomsCommand);
        commands.add(registerOfficerCommand);
        commands.add(changeRankCommand);
        commands.add(listOfficersCommand);
        commands.add(registerDetaineeCommand);
        commands.add(listDetaineesCommand);
        commands.add(releaseDetaineeCommand);
        commands.add(scheduleInterrogationCommand);
        commands.add(scheduleMeetingCommand);

        // Register each command with the handler
        for (Command command : commands) {
            commandHandler.registerCommand(command);
        }

        // Add help and exit commands (need to be added after other commands for help to find them)
        Command helpCommand = new HelpCommand(commandHandler.getAvailableCommands());
        Command exitCommand = new ExitCommand();

        commandHandler.registerCommand(helpCommand);
        commandHandler.registerCommand(exitCommand);
    }

    /**
     * Starts the CLI application and runs the main input loop.
     */
    public void run() {
        System.out.println("Police Management System");
        System.out.println("=======================");
        System.out.println("Type 'help' for a list of available commands, or 'exit' to quit.");
        System.out.println();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                try {
                    commandHandler.processCommand(input);
                } catch (Exception e) {
                    System.err.println("An error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println();
        }
    }
}