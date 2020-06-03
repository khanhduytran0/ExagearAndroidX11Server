package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.Optional;
import com.eltechs.axs.proto.input.annotations.ParamName;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadIdChoice;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.GraphicsContextsManager;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.graphicsContext.ArcMode;
import com.eltechs.axs.xserver.graphicsContext.FillRule;
import com.eltechs.axs.xserver.graphicsContext.FillStyle;
import com.eltechs.axs.xserver.graphicsContext.GraphicsContextParts;
import com.eltechs.axs.xserver.graphicsContext.JoinStyle;
import com.eltechs.axs.xserver.graphicsContext.LineStyle;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import com.eltechs.axs.xserver.graphicsContext.SubwindowMode;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GraphicsContextManipulationRequests extends HandlerObjectBase {

    public enum ClipRectanglesOrdering {
        UNSORTED,
        Y_SORTED,
        YX_SORTED,
        YX_BANDED
    }

    @RequestHandler(opcode = 59)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER"})
	@OOBParam(index = 0)
    public void SetClipRectangles(@RequestParam ClipRectanglesOrdering clipRectanglesOrdering, @RequestParam GraphicsContext graphicsContext, @RequestParam short s, @RequestParam short s2, @RequestParam ByteBuffer byteBuffer) {
    }

    @RequestHandler(opcode = 58)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER"})
    public void SetDashes(@RequestParam GraphicsContext graphicsContext, @RequestParam short s, @RequestParam short s2, @RequestParam ByteBuffer byteBuffer) {
    }

    public GraphicsContextManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 55)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER", "DRAWABLES_MANAGER", "PIXMAPS_MANAGER"})
	@NewXId(index = 2)
    @Optional(
		indexes = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27},
		bits = {
			"FUNCTION", "PLANE_MASK", "FOREGROUND", "BACKGROUND",
			"LINE_WIDTH", "LINE_STYLE", "CAP_STYLE", "JOIN_STYLE",
			"FILL_STYLE", "FILL_RULE", "TILE", "STIPPLE",
			"TILE_STIPPLE_X_ORIGIN", "TILE_STIPPLE_Y_ORIGIN",
			"FONT", "SUBWINDOW_MODE", "GRAPHICS_EXPOSURES",
			"CLIP_X_ORIGIN", "CLIP_Y_ORIGIN", "CLIP_MASK",
			"DASH_OFFSET", "DASHES", "ARC_MODE"
		}
	)
    public void CreateGC(
		XClient xClient, XResponse xResponse, @RequestParam int i, @RequestParam Drawable drawable, @RequestParam @ParamName("mask") Mask<GraphicsContextParts> mask,
		@RequestParam @Width(4) PixelCompositionRule pixelCompositionRule,
		@RequestParam Integer num,
		@RequestParam Integer num2,
		@RequestParam Integer num3,
		@RequestParam Integer num4,
		@RequestParam @Width(4) LineStyle lineStyle,
		@RequestParam Integer num5,
		@RequestParam @Width(4) JoinStyle joinStyle,
		@RequestParam @Width(4) FillStyle fillStyle,
		@RequestParam @Width(4) FillRule fillRule,
		@RequestParam Pixmap pixmap, 
		@RequestParam Pixmap pixmap2, 
		@RequestParam Integer num6, 
		@RequestParam Integer num7,
		@RequestParam Integer num8, 
		@RequestParam @Width(4) SubwindowMode subwindowMode,
		@RequestParam @Width(4) Boolean bool, 
		@RequestParam Integer num9,
		@RequestParam Integer num10, 
		@RequestParam @SpecialNullValue(0) Pixmap pixmap3, 
		@RequestParam Integer num11, 
		@RequestParam Integer num12,
		@RequestParam @Width(4) ArcMode arcMode
	) throws IOException, XProtocolError {
        int i2 = i;
        GraphicsContextsManager graphicsContextsManager = this.xServer.getGraphicsContextsManager();
        GraphicsContext createGraphicsContext = graphicsContextsManager.createGraphicsContext(i2, drawable);
        if (createGraphicsContext == null) {
            throw new BadIdChoice(i2);
        }
        xClient.registerAsOwnerOfGraphicsContext(createGraphicsContext);
        graphicsContextsManager.updateGraphicsContext(createGraphicsContext, mask, pixelCompositionRule, num, num2, num3, num4, lineStyle, num5, joinStyle, fillStyle, fillRule, pixmap, pixmap2, num6, num7, num8, subwindowMode, bool, num9, num10, pixmap3, num11, num12, arcMode);
    }

    @RequestHandler(opcode = 56)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER", "PIXMAPS_MANAGER"})

    @Optional(
		indexes = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24},
		bits = {
			"FUNCTION", "PLANE_MASK", "FOREGROUND", "BACKGROUND",
			"LINE_WIDTH", "LINE_STYLE", "CAP_STYLE", "JOIN_STYLE",
			"FILL_STYLE", "FILL_RULE", "TILE", "STIPPLE",
			"TILE_STIPPLE_X_ORIGIN", "TILE_STIPPLE_Y_ORIGIN",
			"FONT", "SUBWINDOW_MODE", "GRAPHICS_EXPOSURES",
			"CLIP_X_ORIGIN", "CLIP_Y_ORIGIN", "CLIP_MASK",
			"DASH_OFFSET", "DASHES", "ARC_MODE"
		}
	)
    public void ChangeGC(
		@RequestParam GraphicsContext graphicsContext, @RequestParam @ParamName("mask") Mask<GraphicsContextParts> mask,
		@RequestParam @Width(4) PixelCompositionRule pixelCompositionRule,
		@RequestParam Integer num,
		@RequestParam Integer num2,
		@RequestParam Integer num3,
		@RequestParam Integer num4,
		@RequestParam @Width(4) LineStyle lineStyle,
		@RequestParam Integer num5, 
		@RequestParam @Width(4) JoinStyle joinStyle,
		@RequestParam @Width(4) FillStyle fillStyle,
		@RequestParam @Width(4) FillRule fillRule, 
		@RequestParam Pixmap pixmap,
		@RequestParam Pixmap pixmap2, 
		@RequestParam Integer num6,
		@RequestParam Integer num7,
		@RequestParam Integer num8,
		@RequestParam @Width(4) SubwindowMode subwindowMode, 
		@RequestParam @Width(4) Boolean bool,
		@RequestParam Integer num9,
		@RequestParam Integer num10, 
		@RequestParam @SpecialNullValue(0) Pixmap pixmap3,
		@RequestParam Integer num11, 
		@RequestParam Integer num12,
		@RequestParam @Width(4) ArcMode arcMode
	) throws IOException, XProtocolError {
        this.xServer.getGraphicsContextsManager().updateGraphicsContext(graphicsContext, mask, pixelCompositionRule, num, num2, num3, num4, lineStyle, num5, joinStyle, fillStyle, fillRule, pixmap, pixmap2, num6, num7, num8, subwindowMode, bool, num9, num10, pixmap3, num11, num12, arcMode);
    }

    @RequestHandler(opcode = 60)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER"})
    public void FreeGC(XResponse xResponse, @RequestParam GraphicsContext graphicsContext) throws IOException {
        this.xServer.getGraphicsContextsManager().removeGraphicsContext(graphicsContext);
    }
}
