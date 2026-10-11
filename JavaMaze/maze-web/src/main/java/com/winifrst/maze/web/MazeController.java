package com.winifrst.maze.web;

import com.winifrst.maze.MazeService;
import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MazeController {

    private final MazeService mazeService;
    private final MazeSession session;
    private final SvgRenderer renderer;

    public MazeController(MazeService mazeService, MazeSession session, SvgRenderer renderer) {
        this.mazeService = mazeService;
        this.session = session;
        this.renderer = renderer;
    }

    @GetMapping("/")
    public String index(Model model) {
        populateModel(model);
        return "index";
    }

    @PostMapping("/generate")
    public String generate(
            @RequestParam(name = "rows", defaultValue = "10") int rows,
            @RequestParam(name = "cols", defaultValue = "10") int cols,
            Model model) {
        try {
            Maze maze = mazeService.generate(rows, cols);
            session.setMaze(maze);
            model.addAttribute("status", "Сгенерирован лабиринт " + rows + "x" + cols);
        } catch (IllegalArgumentException e) {
            model.addAttribute("status", "Ошибка: " + e.getMessage());
            model.addAttribute("rows", rows);
            model.addAttribute("cols", cols);
            populateModel(model);
            return "index";
        }
        return "redirect:/";
    }

    @PostMapping("/solve")
    public String solve() {
        if (!session.hasMaze()) {
            return "redirect:/";
        }
        Point start = new Point(0, 0);
        Point end = new Point(session.getMaze().getRows() - 1, session.getMaze().getCols() - 1);
        List<Point> path = mazeService.solve(session.getMaze(), start, end);
        session.setStart(start);
        session.setEnd(end);
        session.setSolution(path);
        return "redirect:/";
    }

    private void populateModel(Model model) {
        Maze maze = session.getMaze();
        if (maze != null) {
            model.addAttribute("maze", maze);
            model.addAttribute("svg", renderer.render(maze, session.getSolution()));
            model.addAttribute("rows", maze.getRows());
            model.addAttribute("cols", maze.getCols());
            model.addAttribute("status", buildStatus(maze));
        } else {
            model.addAttribute("rows", 10);
            model.addAttribute("cols", 10);
            model.addAttribute("status", "Сгенерируйте или загрузите лабиринт.");
        }
    }

    private String buildStatus(Maze maze) {
        StringBuilder sb = new StringBuilder();
        sb.append("Лабиринт ").append(maze.getRows()).append('x').append(maze.getCols());
        List<Point> solution = session.getSolution();
        if (solution != null && !solution.isEmpty()) {
            sb.append(" | Длина пути: ").append(solution.size());
        }
        return sb.toString();
    }
}