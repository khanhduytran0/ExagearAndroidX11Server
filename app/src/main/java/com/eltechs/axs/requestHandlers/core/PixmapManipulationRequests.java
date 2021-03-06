package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadIdChoice;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.proto.input.annotations.*;

public class PixmapManipulationRequests extends HandlerObjectBase {
    public PixmapManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 53)
    @Locks({"PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
	@OOBParam(index = 1)
	@NewXId(index = 2)
	@Width(
		indexes = {4, 5},
		values = {2, 2}
	)
	@IntSign(
		unsignedIndexes = {4, 5}
	)
    public void CreatePixmap(XClient xClient, byte b, int i, Drawable drawable, int i2, int i3) throws XProtocolError {
        Drawable createDrawable = this.xServer.getDrawablesManager().createDrawable(i, drawable.getRoot(), i2, i3, b);
        if (createDrawable == null) {
            throw new BadIdChoice(i);
        }
        Pixmap createPixmap = this.xServer.getPixmapsManager().createPixmap(createDrawable);
        Assert.notNull(createPixmap, String.format("Id %d approved by the drawables manager appears to be already used for a pixmap.", new Object[]{Integer.valueOf(i)}));
        xClient.registerAsOwnerOfPixmap(createPixmap);
    }

    @RequestHandler(opcode = 54)
    @Locks({"PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
    public void FreePixmap(XClient xClient, Pixmap pixmap) {
        this.xServer.getPixmapsManager().freePixmap(pixmap);
    }
}
