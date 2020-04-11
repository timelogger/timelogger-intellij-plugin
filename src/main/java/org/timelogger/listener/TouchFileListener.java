package org.timelogger.listener;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.VisibleAreaEvent;
import com.intellij.openapi.editor.event.VisibleAreaListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchFileListener
  implements DocumentListener, EditorMouseListener, FileDocumentManagerListener, VisibleAreaListener
{
  private static final Logger log = LoggerFactory.getLogger(TouchFileListener.class);

  private static Project getProject(Document document) {
    final Editor[] editors = EditorFactory.getInstance().getEditors(document);
    if (editors.length > 0) {
      return editors[0].getProject();
    }
    return null;
  }

  @Override
  public void visibleAreaChanged(@NotNull VisibleAreaEvent e) {
    FileDocumentManager instance = FileDocumentManager.getInstance();
    VirtualFile file = instance.getFile(e.getEditor().getDocument());
    Project project = e.getEditor().getProject();
    appendHeartbeat(file, project, false);
  }

  @Override
  public void documentChanged(@NotNull DocumentEvent event) {
    Document document = event.getDocument();
    FileDocumentManager instance = FileDocumentManager.getInstance();
    VirtualFile file = instance.getFile(document);
    appendHeartbeat(file, getProject(document), false);
  }

  @Override
  public void mouseClicked(@NotNull EditorMouseEvent event) {
    FileDocumentManager instance = FileDocumentManager.getInstance();
    VirtualFile file = instance.getFile(event.getEditor().getDocument());
    Project project = event.getEditor().getProject();
    appendHeartbeat(file, project, false);
  }

  @Override
  public void beforeDocumentSaving(@NotNull Document document) {
    FileDocumentManager instance = FileDocumentManager.getInstance();
    VirtualFile file = instance.getFile(document);
    appendHeartbeat(file, getProject(document), true);
  }

  private void appendHeartbeat(
    final VirtualFile file,
    final Project project,
    final boolean isWrite)
  {
    log.info("heartbeat for {}: {}", project, file);
  }
}
