package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadIdChoice;
import com.eltechs.axs.proto.input.errors.BadMatch;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.CursorsManager;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.proto.input.annotations.*;

public class CursorManipulationRequests extends HandlerObjectBase {
    public CursorManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 93)
    @Locks({"CURSORS_MANAGER", "PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
	@NewXId(index = 1)
	@SpecialNullValue(indexes = {3})
	@Width(
		indexes = {4, 5, 6, 7, 8, 9, 10, 11},
		values = {2, 2, 2, 2, 2, 2, 2, 2}
	)
	@IntSign(
		unsignedIndexes = {4, 5, 6, 7, 8, 9, 10, 11}
	)
    public void CreateCursor(XClient xClient, int i, Pixmap pixmap, Pixmap pixmap2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) throws XProtocolError {
        Drawable backingStore = pixmap.getBackingStore();
        if (pixmap2 != null) {
            Drawable backingStore2 = pixmap2.getBackingStore();
            if (!(backingStore2.getVisual().getDepth() == 1 && backingStore2.getWidth() == backingStore.getWidth() && backingStore2.getHeight() == backingStore.getHeight())) {
                throw new BadMatch();
            }
        }
        if (i8 <= backingStore.getWidth()) {
            if (i9 <= backingStore.getHeight()) {
                CursorsManager cursorsManager = this.xServer.getCursorsManager();
                Cursor createCursor = cursorsManager.createCursor(i, i8, i9, pixmap, pixmap2);
                if (createCursor == null) {
                    throw new BadIdChoice(i);
                }
                cursorsManager.recolorCursor(createCursor, i2, i3, i4, i5, i6, i7);
                xClient.registerAsOwnerOfCursor(createCursor);
                return;
            }
        }
        throw new BadMatch();
    }

    @RequestHandler(opcode = 94)
    @Locks({"CURSORS_MANAGER", "PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
	@NewXId(index = 1)
	@Width(
		indexes = {4, 5, 6, 7, 8, 9, 10, 11},
		values = {2, 2, 2, 2, 2, 2, 2, 2}
	)
	@IntSign(
		unsignedIndexes = {4, 5, 6, 7, 8, 9, 10, 11}
	)
    public void CreateGlyphCursor(XClient xClient, int i, Integer num, Integer num2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) throws XProtocolError {
        int i10 = i;
        CursorsManager cursorsManager = this.xServer.getCursorsManager();
        Cursor createFakeCursor = cursorsManager.createFakeCursor(i10);
        if (createFakeCursor == null) {
            throw new BadIdChoice(i10);
        }
        cursorsManager.recolorCursor(createFakeCursor, i4, i5, i6, i7, i8, i9);
        xClient.registerAsOwnerOfCursor(createFakeCursor);
    }

    @RequestHandler(opcode = 95)
    @Locks({"CURSORS_MANAGER", "PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
    public void FreeCursor(XClient xClient, Cursor cursor) {
        this.xServer.getCursorsManager().freeCursor(cursor);
    }

    @RequestHandler(opcode = 96)
    @Locks({"CURSORS_MANAGER", "DRAWABLES_MANAGER"})
	@Width(
		indexes = {1, 2, 3, 4, 5, 6},
		values = {2, 2, 2, 2, 2, 2}
	)
	@IntSign(
		unsignedIndexes = {1, 2, 3 ,4, 5, 6}
	)
    public void RecolorCursor(Cursor cursor, int i, int i2, int i3, int i4, int i5, int i6) {
        this.xServer.getCursorsManager().recolorCursor(cursor, i, i2, i3, i4, i5, i6);
    }
}
