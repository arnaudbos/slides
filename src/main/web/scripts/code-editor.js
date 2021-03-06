require.config({paths: {'vs': '../scripts/monaco-editor/min/vs'}});

// Before loading vs/editor/editor.main, define a global MonacoEnvironment that overwrites
// the default worker url location (used when creating WebWorkers). The problem here is that
// HTML5 does not allow cross-domain web workers, so we need to proxy the instantiation of
// a web worker through a same-domain script
// window.MonacoEnvironment = {
//     getWorkerUrl: function (workerId, label) {
//         return 'monaco-editor-worker-loader-proxy.js';
//     }
// };

require(['vs/editor/editor.main'], function () {

    // Editor options
    const defaultEditorOptions = {
        // automaticLayout: true,
        lineNumbers: true,
        //theme: 'vs-dark',
        fontFamily: 'Fira Code, monospace',
        fontSize: 24,
        fontLigatures: true
        // FIXME keybinding: format, fix
    };

    // Data
    const extractData = node => ({
        parent: node,
        host: node.querySelector('.editor-panel'),
        consolePanel: node.querySelector('.console-panel'),
        language: node.getAttribute('data-lang'),
        code: node.querySelector('.initialCode').textContent,
        finalCode: node.querySelector('.finalCode').textContent
    });

    // Editor
    const createEditor = ({parent, host, consolePanel, language, code, finalCode}) => {
        const editor = monaco.editor.create(host, {
            ...defaultEditorOptions,
            language,
            value: code,
        });

        return {parent, editor, consolePanel, language, code, finalCode};
    };

    // actions
    const actions = {
        'full-screen': {
            label: 'Toggle Full Screen',
            keyBinding: '',
            run: ({parent, editor}) => () => {
                parent.parentElement.classList.toggle('full-screen');
                editor.layout();
            }
        },
        'toggle-console': {
            label: 'Toggle Console',
            keyBinding: '',
            run: ({parent, editor}) => () => {
                parent.classList.toggle('hide-console');
                editor.layout();
            }
        },
        'reset': {
            label: 'Reset',
            keyBinding: '',
            run: ({editor, code}) => () => {
                editor.setValue(code);
            }
        },
        'load-final': {
            label: 'Load final',
            keyBinding: '',
            run: ({editor, finalCode}) => () => {
                editor.setValue(finalCode);
            }
        },
        'clear-console': {
            label: 'Clear Console',
            keyBinding: '',
            run: ({consolePanel}) => () => {
                const logEntry = consolePanel.querySelector('ul');
                logEntry.innerHTML = '';
            }
        },
        format: {
            label: 'Format',
            keyBinding: '',
            run: ({editor}) => () => {
                const formatAction = editor.getAction('editor.action.formatDocument');
                formatAction.run()
            }
        },
        run: {
            label: 'Run',
            keyBinding: '',
            run: ({editor, language, consolePanel}) => () => {
                const logEntry = consolePanel.querySelector('ul');
                const consoleLog = (message, styleClass) => {
                    const li = document.createElement('li');
                    if (styleClass) {
                        li.setAttribute("class", styleClass);
                    }
                    li.innerHTML = message;
                    logEntry.appendChild(li);
                    li.scrollIntoView();
                };
                const logWithStyle = style => function () {
                    const message = Array.from(arguments)
                        .map(elt => {
                            switch (typeof elt) {
                                case 'object':
                                    return JSON.stringify(elt);
                                default:
                                    return `${elt}`;
                            }
                        }).join(' ');
                    consoleLog(message, style);
                };
                const log = logWithStyle('log');
                const info = logWithStyle('info');
                const warn = logWithStyle('warn');
                const error = logWithStyle('error');

                const value = editor.getValue();
                const headers = new Headers();
                headers.set('Content-Type', 'application/json');
                fetch(`http://localhost:5000/tojs?language=${language}`, {headers, method: 'POST', body: value})
                    .then(response => {
                        if (response.ok) {
                            return response.text();
                        }
                        return response.text()
                            .then(error => Promise.reject(error))
                    })
                    .then(js => {
                        const hack = js.replace(/console\.log/g, 'log')
                            .replace(/console\.info/g, 'info')
                            .replace(/console\.warn/g, 'warn')
                            .replace(/console\.error/g, 'error');
                        try {
                            const result = eval(hack);
                            if (result) {
                                log(`result: ${result}`, 'result');
                            }
                        } catch (e) {
                            error(e.message?e.message: e.toString());
                        }
                    })
                    .catch(error => {
                        console.error(error);
                        log(error, `error`);
                    })
                ;
            }
        }
    };

    // commands
    let count = 0;
    const editorActions = [
        {
            id: 'duplicate',
            label: 'Duplicate',
            keybindings: [
                monaco.KeyMod.WinCtrl | monaco.KeyCode.KEY_D,
                monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_D],
            run: (ed) => {
                const position = ed.getPosition();
                const {lineNumber, column} = position;
                const selection = ed.getSelection();
                let edit;
                let endCursorState;
                const id = {major: 1, minor: ++count};
                if (selection.isEmpty()) {
                    // duplicate line, insert on next line
                    const range = new monaco.Range(lineNumber + 1, 0, lineNumber + 1, 0);
                    const text = ed.getModel().getLineContent(lineNumber) + '\n';
                    endCursorState = {
                        selectionStartLineNumber: lineNumber + 1,
                        positionLineNumber: lineNumber + 1,
                        selectionStartColumn: column,
                        positionColumn: column
                    };
                    edit = {id, range, text, forceMoveMarkers: true};
                } else {
                    // duplicate selection, insert on after the position
                    const range = new monaco.Range(lineNumber, column, lineNumber, column);
                    const text = ed.getModel().getValueInRange(selection);
                    const offsetLine = selection.endLineNumber - selection.startLineNumber;
                    const offsetCol = selection.endColumn - selection.startColumn;
                    endCursorState = {
                        selectionStartLineNumber: lineNumber,
                        positionLineNumber: lineNumber + offsetLine,
                        selectionStartColumn: column,
                        positionColumn: column + offsetCol
                    };
                    edit = {id, range, text, forceMoveMarkers: true};
                }
                // console.info('duplicate', {edit, position, endCursorState});
                ed.executeEdits("slide", [edit], [endCursorState]);
            }
        }
    ];
    const registerActions = params => {
        Object.keys(actions)
            .forEach(key => {
                const btn = params.parent.querySelector(`button.${key}`);
                if (btn) {
                    const {label, run} = actions[key];
                    btn.setAttribute("title", label);
                    btn.addEventListener('click', () => {
                        console.log(label);
                        run(params)();
                    })
                }
            });
        return params;
    };

    const registerEditorActions = params => {
        const {editor} = params;
        editorActions.forEach(action => editor.addAction(action));
        return params;
    };

    Array.from(document.querySelectorAll('.code-editor'))
        .map(extractData)
        .map(createEditor)
        .map(registerActions)
        .map(registerEditorActions)
    ;

});
