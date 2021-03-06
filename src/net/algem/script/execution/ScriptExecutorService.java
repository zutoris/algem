package net.algem.script.execution;

import net.algem.script.common.Script;
import net.algem.script.execution.models.ScriptUserArguments;
import net.algem.script.execution.models.ScriptResult;

public interface ScriptExecutorService {
    /**
     * Execute the given script by the scripting engine
     * @param script the script to execute
     * @param arguments
     * @return script execution result as a ScriptResult object
     * @throws Exception
     */
    public ScriptResult executeScript(Script script, ScriptUserArguments arguments) throws Exception;
}
